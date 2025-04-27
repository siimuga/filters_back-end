package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.CriteriaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CriteriaTypeRepository extends JpaRepository<CriteriaType, Integer> {

    @Query("select ct from CriteriaType ct where ct.name = ?1")
    CriteriaType findByName(String name);
}