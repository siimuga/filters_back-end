package com.example.filters_back_end.dto;

import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {
    private String name;
    private List<CriteriaRequest> criteriaRequests;
}
