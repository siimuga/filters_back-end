package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.FilterCriteria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilterCriteriaRepository extends JpaRepository<FilterCriteria, Integer> {

    @Query("select fc from FilterCriteria fc where fc.filter.id = ?1")
    List<FilterCriteria> findAllByFilterId(Integer filterId);
}