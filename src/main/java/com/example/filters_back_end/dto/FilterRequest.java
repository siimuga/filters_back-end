package com.example.filters_back_end.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class FilterRequest {
    @NotBlank(message = "name cannot be blank")
    @Size(min = 3, message = "min 3 characters")
    @Size(max = 255, message = "max 255 characters")
    private String name;
    @Valid
    private List<CriteriaRequest> criteriaRequests;
}
