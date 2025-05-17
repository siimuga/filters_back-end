package com.example.filters_back_end;

import com.example.filters_back_end.dto.FilterRequest;

public interface ValidationServiceI {
    void validateType(String type);

    void validateRequest(FilterRequest request);
}
