package com.example.filters_back_end;

import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.NameInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("api")
public class FilterController {

    @Resource
    FilterService filterService;

    @GetMapping("criteriaType")
    public List<NameInfo> findAllCriteriaTypes() {
        return filterService.findAllCriteriaTypes();
    }

    @GetMapping("comparingCondition")
    public List<NameInfo> findAllComparingConditions() {
        return filterService.findAllComparingConditions();
    }

    @GetMapping("filters")
    public List<FilterInfo> findAllFiltersWithCriterias() {
        return filterService.findAllFiltersWithCriterias();
    }

}
