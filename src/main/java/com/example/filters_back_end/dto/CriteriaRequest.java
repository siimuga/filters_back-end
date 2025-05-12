package com.example.filters_back_end.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CriteriaRequest {
    @NotNull(message = "type cannot be null")
    private String type;
    @NotNull(message = "condition cannot be null")
    private String condition;
    @NotBlank(message = "value cannot be blank")
    @Size(max = 255, message = "value max 255 characters")
    private String value;
}
