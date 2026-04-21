package com.neoprep.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoprep.domain.StandupLog;
import com.neoprep.domain.UserProfile;
import com.neoprep.dto.StandupDto;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.DayPlanRepository;
import com.neoprep.repository.RoadmapRepository;
import com.neoprep.repository.StandupLogRepository;
import com.neoprep.repository.UserProfileRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class StandupService {

    private final UserProfileRepository userProfileRepository;
    private final RoadmapRepository roadmapRepository;
    private final DayPlanRepository dayPlanRepository;
    private final StandupLogRepository standupLogRepository;
    private final GeminiClient geminiClient;
    private final ObjectMapper objectMapper;

    public StandupService(UserProfileRepository userProfileRepository,
                          RoadmapRepository roadmapRepository,
                          DayPlanRepository dayPlanRepository,
                          StandupLogRepository standupLogRepository,
                          GeminiClient geminiClient,
                          ObjectMapper objectMapper) {
        this.userProfileRepository = userProfileRepository;
        this.roadmapRepository = roadmapRepository;
        this.dayPlanRepository = dayPlanRepository;
        this.standupLogRepository = standupLogRepository;
        this.geminiClient = geminiClient;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public StandupDto generateForUser(Long userId) {
        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        LocalDate today = LocalDate.now();
        return standupLogRepository.findByUserIdAndStandupDate(userId, today)
                .map(this::toDto)
                .orElseGet(() -> {
                    String carryOver = dayPlanRepository.countByRoadmapUserIdAndCompletedTrue(userId) < 35
                            ? "Revisit pending day plans before new topic deep-dive."
                            : "No pending tasks.";
                    String primaryTopic = "Java + DSA";

                    String summary = "Master " + primaryTopic + " today.";
                    String marketWhy = user.getTargetCompanyTier() + " interviews reward speed + correctness on core patterns.";
                    String parsedCarry = carryOver;
                    try {
                        JsonNode root = objectMapper.readTree(geminiClient.generateStandupJson(user.getTargetCompanyTier(), primaryTopic, carryOver));
                        summary = root.hasNonNull("summary") ? root.get("summary").asText() : summary;
                        marketWhy = root.hasNonNull("marketWhy") ? root.get("marketWhy").asText() : marketWhy;
                        parsedCarry = root.hasNonNull("carryOver") ? root.get("carryOver").asText() : parsedCarry;
                    } catch (Exception ignored) {
                        // keep fallback content
                    }

                    StandupLog log = new StandupLog();
                    log.setUser(user);
                    log.setStandupDate(today);
                    log.setSummary(summary);
                    log.setMarketWhy(marketWhy);
                    log.setCarryOver(parsedCarry);
                    return toDto(standupLogRepository.save(log));
                });
    }

    public List<StandupDto> listForUser(Long userId) {
        return standupLogRepository.findTop10ByUserIdOrderByStandupDateDesc(userId).stream()
                .map(this::toDto)
                .toList();
    }

    @Scheduled(cron = "${neoprep.standup.cron:0 0 7 * * *}")
    @Transactional
    public void generateDailyForActiveUsers() {
        userProfileRepository.findAll().stream()
                .filter(user -> !roadmapRepository.findByUserIdOrderByCreatedAtDesc(user.getId()).isEmpty())
                .forEach(user -> generateForUser(user.getId()));
    }

    private StandupDto toDto(StandupLog log) {
        return new StandupDto(log.getId(), log.getStandupDate(), log.getSummary(), log.getMarketWhy(), log.getCarryOver());
    }
}
