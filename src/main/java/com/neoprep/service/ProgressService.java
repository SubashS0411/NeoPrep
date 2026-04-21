package com.neoprep.service;

import com.neoprep.dto.ProgressResponseDto;
import com.neoprep.repository.AchievementRepository;
import com.neoprep.repository.DayPlanRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {

    private final DayPlanRepository dayPlanRepository;
    private final AchievementRepository achievementRepository;

    public ProgressService(DayPlanRepository dayPlanRepository,
                           AchievementRepository achievementRepository) {
        this.dayPlanRepository = dayPlanRepository;
        this.achievementRepository = achievementRepository;
    }

    public ProgressResponseDto getProgress(Long userId) {
        long completedDays = dayPlanRepository.countByRoadmapUserIdAndCompletedTrue(userId);
        int streak = (int) Math.min(35, completedDays);
        String latest = achievementRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .findFirst().map(a -> a.getType()).orElse(null);
        return new ProgressResponseDto(completedDays, streak, latest);
    }
}
