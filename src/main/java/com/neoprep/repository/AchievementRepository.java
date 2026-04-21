package com.neoprep.repository;

import com.neoprep.domain.Achievement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AchievementRepository extends JpaRepository<Achievement, Long> {
    List<Achievement> findByUserIdOrderByCreatedAtDesc(Long userId);
    boolean existsByUserIdAndType(Long userId, String type);
}
