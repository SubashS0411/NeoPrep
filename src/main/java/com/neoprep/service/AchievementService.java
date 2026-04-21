package com.neoprep.service;

import com.neoprep.domain.Achievement;
import com.neoprep.domain.UserProfile;
import com.neoprep.dto.AchievementDto;
import com.neoprep.exception.NotFoundException;
import com.neoprep.repository.AchievementRepository;
import com.neoprep.repository.UserProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AchievementService {

    private final AchievementRepository achievementRepository;
    private final UserProfileRepository userProfileRepository;

    public AchievementService(AchievementRepository achievementRepository,
                              UserProfileRepository userProfileRepository) {
        this.achievementRepository = achievementRepository;
        this.userProfileRepository = userProfileRepository;
    }

    @Transactional
    public AchievementDto unlock7DayStreak(Long userId) {
        if (achievementRepository.existsByUserIdAndType(userId, "STREAK_7")) {
            return achievementRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                    .filter(a -> "STREAK_7".equals(a.getType()))
                    .findFirst()
                    .map(this::toDto)
                    .orElseThrow(() -> new NotFoundException("Achievement not found"));
        }

        UserProfile user = userProfileRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User not found"));

        Achievement achievement = new Achievement();
        achievement.setUser(user);
        achievement.setType("STREAK_7");
        achievement.setLinkedInTemplate("I just completed a 7-day NeoPrep streak focused on DSA + Spring Boot. Consistency compounds. #100DaysOfCode #Java #SpringBoot #DSA");
        achievement = achievementRepository.save(achievement);
        return toDto(achievement);
    }

    public List<AchievementDto> listForUser(Long userId) {
        return achievementRepository.findByUserIdOrderByCreatedAtDesc(userId)
                .stream().map(this::toDto).toList();
    }

    private AchievementDto toDto(Achievement achievement) {
        return new AchievementDto(achievement.getId(), achievement.getType(), achievement.getLinkedInTemplate());
    }
}
