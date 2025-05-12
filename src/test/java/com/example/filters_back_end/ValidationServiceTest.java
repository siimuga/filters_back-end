package com.example.filters_back_end;


import com.example.filters_back_end.dto.CriteriaRequest;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.entities.ComparingCondition;
import com.example.filters_back_end.entities.CriteriaType;
import com.example.filters_back_end.entities.CriteriaTypeCc;
import com.example.filters_back_end.infrastructure.exception.DataNotFoundException;
import com.example.filters_back_end.repos.ComparingConditionRepository;
import com.example.filters_back_end.repos.CriteriaTypeCcRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ValidationServiceTest {

    @InjectMocks
    private ValidationService validationService;

    public static final String TITLE = "Title";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";

    @Mock
    private CriteriaTypeRepository criteriaTypeRepository;

    @Mock
    private ComparingConditionRepository comparingConditionRepository;

    @Mock
    private CriteriaTypeCcRepository criteriaTypeCcRepository;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void validateType_wrongType() {
        String type = "Wrong";

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("type", "This criteria type does not exist");

        when(criteriaTypeRepository.findByName(type)).thenReturn(null);

        assertThrows(DataNotFoundException.class, () -> {
            validationService.validateType(type);
        });
        verify(criteriaTypeRepository, times(1)).findByName(type);
    }

    @Test
    public void validateType_correctType() {
        String type = "Correct";

        when(criteriaTypeRepository.findByName(type)).thenReturn(new CriteriaType());

        assertDoesNotThrow(() -> validationService.validateType(type));
        verify(criteriaTypeRepository, times(1)).findByName(type);
    }

    @Test
    public void validateRequest_wrongType() {
        CriteriaRequest cr1 = createNewCriteriaRequest("Vsop", "Sunny", "5hh");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        when(criteriaTypeRepository.findByName(any())).thenReturn(null);
        when(comparingConditionRepository.findByName(any())).thenReturn(new ComparingCondition());
        when(criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(anyString(), anyString())).thenReturn(new CriteriaTypeCc());

        assertThrows(DataNotFoundException.class, () -> {
            validationService.validateRequest(request);
        });
        verify(criteriaTypeRepository, times(1)).findByName(any());
        verify(comparingConditionRepository, times(1)).findByName(any());
        verify(criteriaTypeCcRepository, times(1)).findByComparingConditionAndCriteriaType(anyString(), anyString());
    }

    @Test
    public void validateRequest_wrongCondition() {
        CriteriaRequest cr1 = createNewCriteriaRequest(TITLE, null, "Job");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        when(criteriaTypeRepository.findByName(any())).thenReturn(new CriteriaType());
        when(comparingConditionRepository.findByName(any())).thenReturn(null);
        when(criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(anyString(), anyString())).thenReturn(new CriteriaTypeCc());

        assertThrows(DataNotFoundException.class, () -> {
            validationService.validateRequest(request);
        });
        verify(criteriaTypeRepository, times(1)).findByName(any());
        verify(comparingConditionRepository, times(1)).findByName(any());
        verify(criteriaTypeCcRepository, times(1)).findByComparingConditionAndCriteriaType(any(), anyString());
    }

    @Test
    public void validateRequest_wrongTypeAndConditionCombination() {
        CriteriaRequest cr1 = createNewCriteriaRequest(TITLE, "Buumer", "Job");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        when(criteriaTypeRepository.findByName(any())).thenReturn(new CriteriaType());
        when(comparingConditionRepository.findByName(any())).thenReturn(new ComparingCondition());
        when(criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(anyString(), anyString())).thenReturn(null);

        assertThrows(DataNotFoundException.class, () -> {
            validationService.validateRequest(request);
        });
        verify(criteriaTypeRepository, times(1)).findByName(any());
        verify(comparingConditionRepository, times(1)).findByName(any());
        verify(criteriaTypeCcRepository, times(1)).findByComparingConditionAndCriteriaType(anyString(), anyString());
    }

    @Test
    public void validateRequest_wrongDateInput() {
        CriteriaRequest cr1 = createNewCriteriaRequest(DATE, "Buumer", "Job");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        when(criteriaTypeRepository.findByName(any())).thenReturn(new CriteriaType());
        when(comparingConditionRepository.findByName(any())).thenReturn(new ComparingCondition());
        when(criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(anyString(), anyString())).thenReturn(new CriteriaTypeCc());

        assertThrows(DataNotFoundException.class, () -> {
            validationService.validateRequest(request);
        });
        verify(criteriaTypeRepository, times(1)).findByName(any());
        verify(comparingConditionRepository, times(1)).findByName(any());
        verify(criteriaTypeCcRepository, times(1)).findByComparingConditionAndCriteriaType(anyString(), anyString());
    }

    @Test
    public void validateRequest_completed() {
        CriteriaRequest cr1 = createNewCriteriaRequest(TITLE, "Buumer", "Job");
        CriteriaRequest cr2 = createNewCriteriaRequest(AMOUNT, "Buumer", "96");
        CriteriaRequest cr3 = createNewCriteriaRequest(DATE, "Buumer", "01.01.2000");
        List<CriteriaRequest> criteriaRequests = List.of(cr1, cr2, cr3);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        when(criteriaTypeRepository.findByName(any())).thenReturn(new CriteriaType());
        when(comparingConditionRepository.findByName(any())).thenReturn(new ComparingCondition());
        when(criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(anyString(), anyString())).thenReturn(new CriteriaTypeCc());

        assertDoesNotThrow(() -> validationService.validateRequest(request));
        verify(criteriaTypeRepository, times(3)).findByName(any());
        verify(comparingConditionRepository, times(3)).findByName(any());
        verify(criteriaTypeCcRepository, times(3)).findByComparingConditionAndCriteriaType(anyString(), anyString());
    }

    private FilterRequest createNewFilterRequest(String name, List<CriteriaRequest> criteriaRequests) {
        return FilterRequest.builder()
                .name(name)
                .criteriaRequests(criteriaRequests)
                .build();
    }

    private CriteriaRequest createNewCriteriaRequest(String type, String condition, Object value) {
        return CriteriaRequest.builder()
                .type(type)
                .condition(condition)
                .value((String) value)
                .build();
    }
}
