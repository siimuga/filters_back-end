package com.example.filters_back_end.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CriteriaTypeEnum {
    TITLE("Title"),
    AMOUNT("Amount"),
    DATE("Date"),;

    private final String textValue;
}
