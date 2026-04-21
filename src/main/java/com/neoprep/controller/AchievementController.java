package com.neoprep.controller;

import com.neoprep.dto.AchievementDto;
import com.neoprep.service.AchievementService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/achievements")
public class AchievementController {

    private final AchievementService achievementService;

    public AchievementController(AchievementService achievementService) {
        this.achievementService = achievementService;
    }

    @PostMapping("/{userId}/streak7")
    public AchievementDto unlock(@PathVariable Long userId) {
        return achievementService.unlock7DayStreak(userId);
    }

    @GetMapping("/{userId}")
    public List<AchievementDto> list(@PathVariable Long userId) {
        return achievementService.listForUser(userId);
    }
}
