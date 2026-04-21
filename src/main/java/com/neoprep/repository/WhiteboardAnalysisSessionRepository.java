package com.neoprep.repository;

import com.neoprep.domain.WhiteboardAnalysisSession;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WhiteboardAnalysisSessionRepository extends JpaRepository<WhiteboardAnalysisSession, Long> {
    List<WhiteboardAnalysisSession> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
