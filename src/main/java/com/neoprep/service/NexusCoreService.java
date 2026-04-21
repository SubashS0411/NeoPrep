package com.neoprep.service;

import com.neoprep.dto.*;
import com.neoprep.repository.DayPlanRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NexusCoreService {

    private final RoadmapService roadmapService;
    private final StandupService standupService;
    private final DayPlanRepository dayPlanRepository;

    public NexusCoreService(RoadmapService roadmapService,
                            StandupService standupService,
                            DayPlanRepository dayPlanRepository) {
        this.roadmapService = roadmapService;
        this.standupService = standupService;
        this.dayPlanRepository = dayPlanRepository;
    }

    @Transactional
    public NexusSyncResponse sync(NexusSyncRequest request) {
        RoadmapRecalculationResponse recalculation = roadmapService.recalculateRoadmap(
                request.roadmapId(),
                new RoadmapRecalculationRequest(request.userId(), request.missedDays(), request.weakTopics())
        );
        StandupDto standup = standupService.generateForUser(request.userId());

        long completed = dayPlanRepository.countByRoadmapUserIdAndCompletedTrue(request.userId());
        String trigger = completed >= 7 ? "STREAK_ELIGIBLE" : "NO_TRIGGER";

        return new NexusSyncResponse(recalculation, standup, trigger);
    }
}
