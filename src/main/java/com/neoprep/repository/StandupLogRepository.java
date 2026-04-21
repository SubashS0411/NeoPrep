package com.neoprep.repository;

import com.neoprep.domain.StandupLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface StandupLogRepository extends JpaRepository<StandupLog, Long> {
    Optional<StandupLog> findByUserIdAndStandupDate(Long userId, LocalDate standupDate);
    List<StandupLog> findTop10ByUserIdOrderByStandupDateDesc(Long userId);
}
