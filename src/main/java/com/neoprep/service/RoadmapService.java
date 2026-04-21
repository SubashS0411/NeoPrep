package com.neoprep.service;

import com.neoprep.domain.*;
import com.neoprep.dto.*;
import com.neoprep.exception.BadRequestException;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.IntStream;

@Service
public class RoadmapService {

    private final UserProfileRepository userProfileRepository;
    private final RoadmapRepository roadmapRepository;
    private final DayPlanRepository dayPlanRepository;
    private final GeminiClient geminiClient;
    private final RoadmapJsonParser roadmapJsonParser;
    private final SkillWeaknessRepository skillWeaknessRepository;
    private final QuizAttemptRepository quizAttemptRepository;
    private final RoadmapRecalculationAuditRepository recalculationAuditRepository;

    public RoadmapService(UserProfileRepository userProfileRepository,
                          RoadmapRepository roadmapRepository,
                          DayPlanRepository dayPlanRepository,
                          GeminiClient geminiClient,
                          RoadmapJsonParser roadmapJsonParser,
                          SkillWeaknessRepository skillWeaknessRepository,
                          QuizAttemptRepository quizAttemptRepository,
                          RoadmapRecalculationAuditRepository recalculationAuditRepository) {
        this.userProfileRepository = userProfileRepository;
        this.roadmapRepository = roadmapRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.geminiClient = geminiClient;
        this.roadmapJsonParser = roadmapJsonParser;
        this.skillWeaknessRepository = skillWeaknessRepository;
        this.quizAttemptRepository = quizAttemptRepository;
        this.recalculationAuditRepository = recalculationAuditRepository;
    }

    @Transactional
    public RoadmapResponseDto generateRoadmap(OnboardingRequest request) {
        UserProfile user = userProfileRepository.findByEmail(request.email())
                .orElseGet(() -> {
                    UserProfile profile = new UserProfile();
                    profile.setEmail(request.email());
                    return profile;
                });
        user.setTargetCompanyTier(request.targetCompanyTier());
        user = userProfileRepository.save(user);

        RoadmapGeneration generated = tryGenerateWithRetry(request.jobDescription(), request.targetCompanyTier());

        Roadmap roadmap = new Roadmap();
        roadmap.setUser(user);
        roadmap.setRoadmapName(generated.roadmapName());
        roadmap.setTargetCompanyTier(generated.targetCompanyTier());
        Roadmap savedRoadmap = roadmapRepository.save(roadmap);

        List<DayPlan> dayPlans = generated.days().stream().map(day -> {
            DayPlan plan = new DayPlan();
            plan.setRoadmap(savedRoadmap);
            plan.setDayNumber(day.dayNumber());
            plan.setFocusArea(parseFocusArea(day.focusArea()));
            plan.setPrimaryTopic(day.primaryTopic());
            plan.setActionableTask(day.actionableTask());
            plan.setLeetcodePattern(day.leetcodePattern());
            return plan;
        }).toList();

        dayPlanRepository.saveAll(dayPlans);
        return mapRoadmap(savedRoadmap.getId());
    }

    @Transactional
    public RoadmapRecalculationResponse recalculateRoadmap(Long roadmapId, RoadmapRecalculationRequest request) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new NotFoundException("Roadmap not found"));

        if (!roadmap.getUser().getId().equals(request.userId())) {
            throw new BadRequestException("Roadmap does not belong to provided userId");
        }

        List<RecalculationReasonDto> reasons = collectRecalculationReasons(roadmap.getUser(), request);
        String weakTopic = reasons.isEmpty() ? "Interview Foundations" : reasons.get(0).reason();

        List<DayPlan> days = dayPlanRepository.findByRoadmapIdOrderByDayNumber(roadmapId);
        List<DayPlan> pending = days.stream().filter(day -> !day.isCompleted()).toList();
        int changed = 0;
        for (int i = 0; i < pending.size(); i++) {
            DayPlan day = pending.get(i);
            if (i < 5 || (request.missedDays() != null && request.missedDays().contains(day.getDayNumber()))) {
                day.setPrimaryTopic(weakTopic + " Reinforcement " + day.getDayNumber());
                day.setActionableTask("Prioritize weakness: " + weakTopic + ". Complete one timed practice + one revision block.");
                day.setLeetcodePattern("Adaptive Focus");
                changed++;
            }
        }

        if (changed > 0) {
            dayPlanRepository.saveAll(pending);
        }

        RoadmapRecalculationAudit audit = new RoadmapRecalculationAudit();
        audit.setRoadmap(roadmap);
        audit.setChangedDays(changed);
        audit.setReasonSummary("Adaptive recalculation based on missed days and weakness signals.");
        audit.setReasonMetadata(reasons.stream()
                .map(reason -> reason.reason() + "|" + reason.weight() + "|" + reason.source())
                .reduce((a, b) -> a + ";" + b)
                .orElse("no-signals"));
        recalculationAuditRepository.save(audit);

        return new RoadmapRecalculationResponse(roadmapId, changed, reasons, mapRoadmap(roadmapId));
    }

    public RoadmapResponseDto mapRoadmap(Long roadmapId) {
        Roadmap roadmap = roadmapRepository.findById(roadmapId)
                .orElseThrow(() -> new NotFoundException("Roadmap not found"));
        List<RoadmapDayDto> days = dayPlanRepository.findByRoadmapIdOrderByDayNumber(roadmapId).stream()
                .map(day -> new RoadmapDayDto(
                        day.getDayNumber(),
                        day.getFocusArea().name(),
                        day.getPrimaryTopic(),
                        day.getActionableTask(),
                        day.getLeetcodePattern(),
                        day.isCompleted()
                )).toList();

        return new RoadmapResponseDto(roadmap.getId(), roadmap.getRoadmapName(), roadmap.getTargetCompanyTier(), days);
    }

    public RoadmapDayDto getDay(Long roadmapId, Integer dayNumber) {
        DayPlan day = dayPlanRepository.findByRoadmapIdAndDayNumber(roadmapId, dayNumber)
                .orElseThrow(() -> new NotFoundException("Day plan not found"));
        return new RoadmapDayDto(day.getDayNumber(), day.getFocusArea().name(), day.getPrimaryTopic(), day.getActionableTask(), day.getLeetcodePattern(), day.isCompleted());
    }

    @Transactional
    public void markDayComplete(Long roadmapId, Integer dayNumber) {
        DayPlan day = dayPlanRepository.findByRoadmapIdAndDayNumber(roadmapId, dayNumber)
                .orElseThrow(() -> new NotFoundException("Day plan not found"));
        day.setCompleted(true);
        dayPlanRepository.save(day);
    }

    private List<RecalculationReasonDto> collectRecalculationReasons(UserProfile user, RoadmapRecalculationRequest request) {
        List<RecalculationReasonDto> reasons = new ArrayList<>();

        if (request.weakTopics() != null) {
            for (String topic : request.weakTopics()) {
                if (topic == null || topic.isBlank()) {
                    continue;
                }
                SkillWeakness weakness = new SkillWeakness();
                weakness.setUser(user);
                weakness.setTopic(topic.trim());
                weakness.setWeight(1.0);
                weakness.setSource("manual");
                skillWeaknessRepository.save(weakness);
                reasons.add(new RecalculationReasonDto(topic.trim(), 1.0, "manual"));
            }
        }

        if (request.missedDays() != null && !request.missedDays().isEmpty()) {
            reasons.add(new RecalculationReasonDto("Missed days: " + request.missedDays(), 0.8, "schedule"));
        }

        quizAttemptRepository.findTop10ByUserIdOrderByCreatedAtDesc(user.getId()).stream()
                .filter(attempt -> attempt.getMaxScore() > 0)
                .filter(attempt -> ((double) attempt.getScore() / attempt.getMaxScore()) < 0.7)
                .limit(3)
                .forEach(attempt -> reasons.add(new RecalculationReasonDto(
                        attempt.getTopic(),
                        0.9,
                        "quiz"
                )));

        if (reasons.isEmpty()) {
            reasons.add(new RecalculationReasonDto("General consistency", 0.5, "fallback"));
        }

        return reasons;
    }

    private RoadmapGeneration tryGenerateWithRetry(String jobDescription, String targetCompanyTier) {
        Exception lastException = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                String response = geminiClient.generateRoadmapJson(jobDescription, targetCompanyTier);
                return roadmapJsonParser.parseAndValidate(response);
            } catch (Exception ex) {
                lastException = ex;
            }
        }
        return fallbackRoadmap(targetCompanyTier, lastException);
    }

    private RoadmapGeneration fallbackRoadmap(String targetCompanyTier, Exception cause) {
        List<RoadmapGenerationDay> days = IntStream.rangeClosed(1, 35)
                .mapToObj(day -> new RoadmapGenerationDay(
                        day,
                        day % 2 == 0 ? "SPRING_BOOT" : "DSA",
                        day % 2 == 0 ? "Spring Boot Foundation " + day : "DSA Foundation " + day,
                        day % 2 == 0 ? "Build one REST endpoint and unit tests." : "Solve two problems and document complexity.",
                        day % 2 == 0 ? null : "Sliding Window"
                )).toList();
        String suffix = cause == null ? "" : " (resilient fallback)";
        return new RoadmapGeneration("Fallback 35-Day Roadmap", targetCompanyTier + suffix, days);
    }

    private FocusArea parseFocusArea(String value) {
        try {
            return FocusArea.valueOf(value.trim().toUpperCase(Locale.ROOT));
        } catch (Exception ex) {
            return FocusArea.DSA;
        }
    }
}
