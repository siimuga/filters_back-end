package com.example.filters_back_end.infrastructure;

import com.example.filters_back_end.infrastructure.error.ApiError;
import com.example.filters_back_end.infrastructure.exception.DataNotFoundException;
import com.example.filters_back_end.infrastructure.exception.DateTimeParseException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@ControllerAdvice
public class RestExceptionHandler {

    public static final String INVALID_INPUT = "Wrong input";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationErrors(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        ApiError apiError = createNewApiError(INVALID_INPUT, BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiError> handleConstraintViolation(ConstraintViolationException exception) {
        Map<String, String> errors = new HashMap<>();
        exception.getConstraintViolations().forEach(violation -> {
            String field = getFieldValue(violation);
            errors.put(field, violation.getMessage());
        });
        ApiError apiError = createNewApiError(INVALID_INPUT, BAD_REQUEST.value(), errors);
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler(DateTimeParseException.class)
    public ResponseEntity<ApiError> handleDateTimeParsingViolation(DateTimeParseException exception) {
        ApiError apiError = createNewApiError(INVALID_INPUT, BAD_REQUEST.value(), exception.getDetail());
        return new ResponseEntity<>(apiError, BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiError> handleDataNotFoundException(DataNotFoundException exception) {
        ApiError apiError = createNewApiError(exception.getTitle(), NOT_FOUND.value(), exception.getDetail());
        return new ResponseEntity<>(apiError, NOT_FOUND);
    }

    private ApiError createNewApiError(String title, int statusCode, Object detail) {
        return ApiError.builder()
                .title(title)
                .statusCode(statusCode)
                .detail(detail)
                .build();
    }

    private static String getFieldValue(ConstraintViolation<?> violation) {
        String fullPath = violation.getPropertyPath().toString();
        return fullPath.contains(".")
                ? fullPath.substring(fullPath.lastIndexOf('.') + 1)
                : fullPath;
    }
}