package com.neoprep.repository;

import com.neoprep.domain.OutreachDraft;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutreachDraftRepository extends JpaRepository<OutreachDraft, Long> {
    List<OutreachDraft> findTop10ByUserIdOrderByCreatedAtDesc(Long userId);
}
