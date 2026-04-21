package com.neoprep.repository;

import com.neoprep.domain.DayPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface DayPlanRepository extends JpaRepository<DayPlan, Long> {
    List<DayPlan> findByRoadmapIdOrderByDayNumber(Long roadmapId);
    Optional<DayPlan> findByRoadmapIdAndDayNumber(Long roadmapId, Integer dayNumber);
    long countByRoadmapUserIdAndCompletedTrue(Long userId);
}
