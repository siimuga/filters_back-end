package com.example.filters_back_end.dto;

import lombok.Data;

@Data
public class CriteriaRequest {

    private String type;
    private String condition;
    private String value;
}
