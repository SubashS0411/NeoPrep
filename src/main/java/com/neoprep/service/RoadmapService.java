package com.neoprep.service;

import com.neoprep.domain.*;
import com.neoprep.dto.*;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.DayPlanRepository;
import com.neoprep.repository.RoadmapRepository;
import com.neoprep.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RoadmapService {

    private final UserProfileRepository userProfileRepository;
    private final RoadmapRepository roadmapRepository;
    private final DayPlanRepository dayPlanRepository;
    private final GeminiClient geminiClient;
    private final RoadmapJsonParser roadmapJsonParser;

    public RoadmapService(UserProfileRepository userProfileRepository,
                          RoadmapRepository roadmapRepository,
                          DayPlanRepository dayPlanRepository,
                          GeminiClient geminiClient,
                          RoadmapJsonParser roadmapJsonParser) {
        this.userProfileRepository = userProfileRepository;
        this.roadmapRepository = roadmapRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.geminiClient = geminiClient;
        this.roadmapJsonParser = roadmapJsonParser;
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

    private RoadmapGeneration tryGenerateWithRetry(String jobDescription, String targetCompanyTier) {
        Exception last = null;
        for (int attempt = 1; attempt <= 3; attempt++) {
            try {
                String response = geminiClient.generateRoadmapJson(jobDescription, targetCompanyTier);
                return roadmapJsonParser.parseAndValidate(response);
            } catch (Exception ex) {
                last = ex;
            }
        }
        return fallbackRoadmap(targetCompanyTier, last);
    }

    private RoadmapGeneration fallbackRoadmap(String targetCompanyTier, Exception cause) {
        List<RoadmapGenerationDay> days = java.util.stream.IntStream.rangeClosed(1, 35)
                .mapToObj(day -> new RoadmapGenerationDay(
                        day,
                        day % 2 == 0 ? "SPRING_BOOT" : "DSA",
                        day % 2 == 0 ? "Spring Boot Foundation " + day : "DSA Foundation " + day,
                        day % 2 == 0 ? "Build one REST endpoint and unit tests." : "Solve two problems and document complexity.",
                        day % 2 == 0 ? null : "Sliding Window"
                )).toList();
        return new RoadmapGeneration("Fallback 35-Day Roadmap", targetCompanyTier + " (fallback)", days);
    }

    private FocusArea parseFocusArea(String value) {
        try {
            return FocusArea.valueOf(value.trim().toUpperCase());
        } catch (Exception ex) {
            return FocusArea.DSA;
        }
    }
}
