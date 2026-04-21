package com.neoprep.repository;

import com.neoprep.domain.QuizAttempt;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuizAttemptRepository extends JpaRepository<QuizAttempt, Long> {
    List<QuizAttempt> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
