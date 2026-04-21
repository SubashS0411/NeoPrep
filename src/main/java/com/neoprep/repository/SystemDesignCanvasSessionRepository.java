package com.neoprep.repository;

import com.neoprep.domain.SystemDesignCanvasSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SystemDesignCanvasSessionRepository extends JpaRepository<SystemDesignCanvasSession, Long> {
    List<SystemDesignCanvasSession> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
