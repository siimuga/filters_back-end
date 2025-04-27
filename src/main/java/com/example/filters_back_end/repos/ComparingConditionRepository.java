package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.ComparingCondition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ComparingConditionRepository extends JpaRepository<ComparingCondition, Integer> {

    @Query("select cc from ComparingCondition cc where cc.name = ?1")
    ComparingCondition findByName(String name);
}