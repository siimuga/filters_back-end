package com.example.filters_back_end;

import com.example.filters_back_end.dto.CriteriaRequest;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.infrastructure.exception.DataNotFoundException;
import com.example.filters_back_end.repos.ComparingConditionRepository;
import com.example.filters_back_end.repos.CriteriaTypeCcRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ValidationService {

    public static final String INVALID_INPUT = "Wrong input";
    public static final String DATE_PATTERN = "dd.MM.yyyy";
    public static final String DATE = "Date";

    @Resource
    private CriteriaTypeRepository criteriaTypeRepository;

    @Resource
    private CriteriaTypeCcRepository criteriaTypeCcRepository;

    @Resource
    private ComparingConditionRepository comparingConditionRepository;

    public void validateRequest(FilterRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        int index = 0;

        for (CriteriaRequest criteriaRequest : request.getCriteriaRequests()) {
            String type = criteriaRequest.getType();
            String condition = criteriaRequest.getCondition();
            if (criteriaTypeRepository.findByName(type) == null) {
                errors.put("criteriaRequests[" + index + "].type", "This criteria type '" + type + "' does not exist");
            }
            if (comparingConditionRepository.findByName(condition) == null) {
                errors.put("criteriaRequests[" + index + "].condition", "This criteria condition '" + condition + "' does not exist");
            }
            if (criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(condition, type) == null) {
                errors.put("criteriaRequests[" + index + "]", "Invalid combination of type and condition");
            }
            if (DATE.equals(type)) {
                validateDate(criteriaRequest, errors, index);
            }
            index++;
        }

        if (!errors.isEmpty()) {
            throw new DataNotFoundException(INVALID_INPUT, errors);
        }
    }

    private static void validateDate(CriteriaRequest criteriaRequest, Map<String, String> errors, int index) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);
        String date = criteriaRequest.getValue();
        try {
            LocalDate.parse(criteriaRequest.getValue(), formatter);
        } catch (java.time.format.DateTimeParseException e) {
            errors.put("criteriaRequests[" + index + "]", "Invalid date format '" + date + "'");
        }
    }

    public void validateType(String type) {
        Map<String, String> errors = new LinkedHashMap<>();
        if (criteriaTypeRepository.findByName(type) == null) {
            errors.put("type", "This criteria type '" + type + "' does not exist");
        }
        if (!errors.isEmpty()) {
            throw new DataNotFoundException(INVALID_INPUT, errors);
        }
    }
}
