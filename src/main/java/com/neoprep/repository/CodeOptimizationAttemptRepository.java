package com.neoprep.repository;

import com.neoprep.domain.CodeOptimizationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeOptimizationAttemptRepository extends JpaRepository<CodeOptimizationAttempt, Long> {
    Optional<CodeOptimizationAttempt> findTopByUserIdAndProblemNameOrderByCreatedAtDesc(Long userId, String problemName);
}
