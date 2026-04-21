package com.neoprep.controller;

import com.neoprep.dto.NexusSyncRequest;
import com.neoprep.dto.NexusSyncResponse;
import com.neoprep.service.NexusCoreService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/nexus")
public class NexusCoreController {

    private final NexusCoreService nexusCoreService;

    public NexusCoreController(NexusCoreService nexusCoreService) {
        this.nexusCoreService = nexusCoreService;
    }

    @PostMapping("/sync")
    public NexusSyncResponse sync(@Valid @RequestBody NexusSyncRequest request) {
        return nexusCoreService.sync(request);
    }
}
