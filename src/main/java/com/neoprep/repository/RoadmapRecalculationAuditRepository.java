package com.neoprep.repository;

import com.neoprep.domain.RoadmapRecalculationAudit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoadmapRecalculationAuditRepository extends JpaRepository<RoadmapRecalculationAudit, Long> {
    List<RoadmapRecalculationAudit> findTop10ByRoadmapIdOrderByCreatedAtDesc(Long roadmapId);
}
