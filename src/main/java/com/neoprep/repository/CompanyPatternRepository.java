package com.neoprep.repository;

import com.neoprep.domain.CompanyPattern;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CompanyPatternRepository extends JpaRepository<CompanyPattern, Long> {
    List<CompanyPattern> findByCompanyIgnoreCaseAndTierIgnoreCaseOrderByFreshnessScoreDesc(String company, String tier);
}
