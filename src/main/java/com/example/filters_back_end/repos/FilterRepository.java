package com.example.filters_back_end.repos;

import com.example.filters_back_end.entities.Filter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilterRepository extends JpaRepository<Filter, Integer> {
}