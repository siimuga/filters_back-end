package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.CriteriaTypeCc;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CriteriaTypeCcRepository extends JpaRepository<CriteriaTypeCc, Integer> {

    @Query("select ctc from CriteriaTypeCc ctc where ctc.comparingCondition.name = ?1 and ctc.criteriaType.name = ?2")
    CriteriaTypeCc findByComparingConditionAndCriteriaType(String comparingCondition, String criteriaType);

    @Query("select ctc from CriteriaTypeCc ctc where ctc.criteriaType.name = ?1")
    List<CriteriaTypeCc> findAllByCriteriaType(String criteriaType);
}