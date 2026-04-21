package com.neoprep.controller;

import com.neoprep.dto.StandupDto;
import com.neoprep.service.StandupService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/standups")
public class StandupController {

    private final StandupService standupService;

    public StandupController(StandupService standupService) {
        this.standupService = standupService;
    }

    @PostMapping("/{userId}/generate")
    public StandupDto generate(@PathVariable Long userId) {
        return standupService.generateForUser(userId);
    }

    @GetMapping("/{userId}")
    public List<StandupDto> list(@PathVariable Long userId) {
        return standupService.listForUser(userId);
    }
}
