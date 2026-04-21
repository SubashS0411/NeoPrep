package com.neoprep.repository;

import com.neoprep.domain.ReadinessScore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReadinessScoreRepository extends JpaRepository<ReadinessScore, Long> {
    List<ReadinessScore> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
