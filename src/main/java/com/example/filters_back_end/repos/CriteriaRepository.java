package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.Criteria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CriteriaRepository extends JpaRepository<Criteria, Integer> {
}