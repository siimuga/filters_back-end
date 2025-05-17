package com.example.filters_back_end;

import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.dto.NameInfo;

import java.util.List;

public interface FilterServiceI {
    List<NameInfo> findAllCriteriaTypes();

    List<NameInfo> findAllComparingConditionsByType(String type);

    List<FilterInfo> findAllFiltersWithCriterias();

    void addFilter(FilterRequest request);
}
