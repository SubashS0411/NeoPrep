package com.neoprep.repository;

import com.neoprep.domain.SkillWeakness;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillWeaknessRepository extends JpaRepository<SkillWeakness, Long> {
    List<SkillWeakness> findByUserIdOrderByWeightDesc(Long userId);
}
