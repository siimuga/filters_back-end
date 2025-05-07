package com.example.filters_back_end.infrastructure.exception;

import lombok.Data;

@Data
public class DataNotFoundException extends RuntimeException {
    private final String title;
    private final Object detail;

    public DataNotFoundException(String title, Object detail) {
        super(title);
        this.title = title;
        this.detail = detail;
    }
}
