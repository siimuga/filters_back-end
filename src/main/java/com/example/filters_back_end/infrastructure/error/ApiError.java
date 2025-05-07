package com.example.filters_back_end.infrastructure.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApiError {
    private String title;
    private Integer statusCode;
    private Object detail;
}

