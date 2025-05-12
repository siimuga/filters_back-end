package com.example.filters_back_end;


import com.example.filters_back_end.dto.CriteriaInfo;
import com.example.filters_back_end.dto.CriteriaRequest;
import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.Criteria;
import com.example.filters_back_end.entities.CriteriaTypeCc;
import com.example.filters_back_end.entities.Filter;
import com.example.filters_back_end.entities.FilterCriteria;
import com.example.filters_back_end.infrastructure.exception.DataNotFoundException;
import com.example.filters_back_end.mappers.CriteriaMapper;
import com.example.filters_back_end.mappers.CriteriaTypeCcMapper;
import com.example.filters_back_end.mappers.CriteriaTypeMapper;
import com.example.filters_back_end.repos.CriteriaRepository;
import com.example.filters_back_end.repos.CriteriaTypeCcRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import com.example.filters_back_end.repos.FilterCriteriaRepository;
import com.example.filters_back_end.repos.FilterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FilterServiceTest {

    @InjectMocks
    private FilterService filterService;

    @Mock
    private ValidationService validationService;

    public static final String INVALID_INPUT = "Wrong input";
    public static final String TITLE = "Title";
    public static final String AMOUNT = "Amount";
    public static final String DATE = "Date";


    @Mock
    private FilterCriteriaRepository filterCriteriaRepository;

    @Mock
    private CriteriaTypeRepository criteriaTypeRepository;

    @Mock
    private CriteriaTypeCcRepository criteriaTypeCcRepository;

    @Mock
    private FilterRepository filterRepository;

    @Mock
    private CriteriaRepository criteriaRepository;

    @Mock
    private CriteriaMapper criteriaMapper;

    @Mock
    private CriteriaTypeMapper criteriaTypeMapper;

    @Mock
    private CriteriaTypeCcMapper criteriaTypeCcMapper;

    @BeforeEach
    void setUp() {
        initMocks(this);
    }

    @Test
    public void findAllCriteriaTypes_completed() {
        filterService.findAllCriteriaTypes();
        verify(criteriaTypeRepository, times(1)).findAll();
        verify(criteriaTypeMapper, times(1)).criteriaTypesToNameInfos(anyList());
    }

    @Test
    public void findAllComparingConditionsByType_wrongType() {
        String type = "Wrong";

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("type", "This criteria type '" + type + "' does not exist");

        doThrow(new DataNotFoundException(INVALID_INPUT, errors))
                .when(validationService).validateType(anyString());
        assertThrows(DataNotFoundException.class, () -> {
            filterService.findAllComparingConditionsByType(type);
        });
        verify(validationService, times(1)).validateType(anyString());
        verify(criteriaTypeCcRepository, never()).findAllByCriteriaType(type);
        verify(criteriaTypeCcMapper, never()).criteriaTypeCcsToNameInfos(anyList());
    }

    @Test
    public void findAllComparingConditionsByType_correctType() {
        String type = "Correct";

        List<CriteriaTypeCc> mockEntities = List.of(new CriteriaTypeCc(), new CriteriaTypeCc());
        NameInfo ni1 = createNewNameInfo("Alfa");
        NameInfo ni2 = createNewNameInfo("Beta");
        List<NameInfo> expectedResult = List.of(ni1, ni2);
        doNothing().when(validationService).validateType(type);
        when(criteriaTypeCcRepository.findAllByCriteriaType(type)).thenReturn(mockEntities);
        when(criteriaTypeCcMapper.criteriaTypeCcsToNameInfos(mockEntities)).thenReturn(expectedResult);

        List<NameInfo> result = filterService.findAllComparingConditionsByType(type);

        assertEquals(expectedResult, result);
        verify(validationService, times(1)).validateType(type);
        verify(criteriaTypeCcRepository, times(1)).findAllByCriteriaType(type);
        verify(criteriaTypeCcMapper, times(1)).criteriaTypeCcsToNameInfos(mockEntities);
    }

    @Test
    public void findAllFiltersWithCriterias_shouldReturnFilterInfos() {
        FilterCriteria fc1 = createNewTitleFilterCriteria();
        FilterCriteria fc2 = createNewAmountFilterCriteria();
        FilterCriteria fc3 = createNewDateFilterCriteria();
        CriteriaInfo criteriaInfo = new CriteriaInfo();

        when(criteriaMapper.criteriaToCriteriaInfo(any())).thenReturn(criteriaInfo);
        when(filterCriteriaRepository.findAll()).thenReturn(List.of(fc1, fc2, fc3));

        List<FilterInfo> result = filterService.findAllFiltersWithCriterias();

        assertNotNull(result);

        verify(filterCriteriaRepository, times(1)).findAll();
    }

    @Test
    public void addFilter_invalidInput() {
        CriteriaRequest cr1 = createNewCriteriaRequest("Umpa", "Jbno");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Name", criteriaRequests);

        Map<String, String> errors = new LinkedHashMap<>();
        errors.put("type", "This criteria type does not exist");

        doThrow(new DataNotFoundException(INVALID_INPUT, errors))
                .when(validationService).validateRequest(request);
        assertThrows(DataNotFoundException.class, () -> {
            filterService.addFilter(request);
        });
        verify(validationService, times(1)).validateRequest(any());
        verify(filterRepository, never()).save(any());
        verify(criteriaRepository, never()).saveAll(anyList());
        verify(filterCriteriaRepository, never()).saveAll(anyList());
    }

    @Test
    public void addFilter_wrongDateFormatInput() {
        CriteriaRequest cr1 = createNewCriteriaRequest(DATE, "Asiis");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        doNothing().when(validationService).validateRequest(request);
        when(filterRepository.save(any())).thenReturn(new Filter());
        when(criteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(filterCriteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        assertThrows(java.time.format.DateTimeParseException.class, () -> {
            filterService.addFilter(request);
        });
        verify(validationService, times(1)).validateRequest(any());
        verify(filterRepository, times(1)).save(any());
        verify(criteriaRepository, never()).saveAll(anyList());
        verify(filterCriteriaRepository, never()).saveAll(anyList());
    }

    @Test
    public void addFilter_wrongAmountInput() {
        CriteriaRequest cr1 = createNewCriteriaRequest(AMOUNT, "5hh");
        List<CriteriaRequest> criteriaRequests = List.of(cr1);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        doNothing().when(validationService).validateRequest(request);
        when(filterRepository.save(any())).thenReturn(new Filter());
        when(criteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(filterCriteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        assertThrows(NumberFormatException.class, () -> {
            filterService.addFilter(request);
        });
        verify(validationService, times(1)).validateRequest(any());
        verify(filterRepository, times(1)).save(any());
        verify(criteriaRepository, never()).saveAll(anyList());
        verify(filterCriteriaRepository, never()).saveAll(anyList());
    }

    @Test
    public void addFilter_completed() {
        CriteriaRequest cr1 = createNewCriteriaRequest(TITLE, "Jbno");
        CriteriaRequest cr2 = createNewCriteriaRequest(AMOUNT, "5");
        CriteriaRequest cr3 = createNewCriteriaRequest(DATE, "01.11.1939");
        List<CriteriaRequest> criteriaRequests = List.of(cr1, cr2, cr3);
        FilterRequest request = createNewFilterRequest("Noname", criteriaRequests);

        doNothing().when(validationService).validateRequest(request);
        when(filterRepository.save(any())).thenReturn(new Filter());
        when(criteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());
        when(filterCriteriaRepository.saveAll(anyList())).thenReturn(new ArrayList<>());

        assertDoesNotThrow(() -> filterService.addFilter(request));
        verify(validationService, times(1)).validateRequest(any());
        verify(filterRepository, times(1)).save(any());
        verify(criteriaRepository, times(1)).saveAll(anyList());
        verify(filterCriteriaRepository, times(1)).saveAll(anyList());
    }

    private FilterRequest createNewFilterRequest(String name, List<CriteriaRequest> criteriaRequests) {
        return FilterRequest.builder()
                .name(name)
                .criteriaRequests(criteriaRequests)
                .build();
    }

    private CriteriaRequest createNewCriteriaRequest(String type, Object value) {
        return CriteriaRequest.builder()
                .type(type)
                .condition("Sunny")
                .value((String) value)
                .build();
    }

    private FilterCriteria createNewTitleFilterCriteria() {
        Filter filter = createNewFilter("TitleFilter");
        Criteria criteria = createNewTitleCriteria();

        return FilterCriteria.builder()
                .filter(filter)
                .criteria(criteria)
                .build();
    }

    private FilterCriteria createNewDateFilterCriteria() {
        Filter filter = createNewFilter("DateFilter");
        Criteria criteria = createNewDateCriteria();

        return FilterCriteria.builder()
                .filter(filter)
                .criteria(criteria)
                .build();
    }

    private FilterCriteria createNewAmountFilterCriteria() {
        Filter filter = createNewFilter("AmountFilter");
        Criteria criteria = createNewAmountCriteria();

        return FilterCriteria.builder()
                .filter(filter)
                .criteria(criteria)
                .build();
    }

    private Criteria createNewAmountCriteria() {
        Criteria criteria = new Criteria();
        criteria.setAmount(404);
        criteria.setCriteriaTypeCc(new CriteriaTypeCc());
        return criteria;
    }

    private Criteria createNewDateCriteria() {
        Criteria criteria = new Criteria();
        criteria.setDate(new Date());
        criteria.setCriteriaTypeCc(new CriteriaTypeCc());
        return criteria;
    }

    private Criteria createNewTitleCriteria() {
        Criteria criteria = new Criteria();
        criteria.setTitle("LOL");
        criteria.setCriteriaTypeCc(new CriteriaTypeCc());
        return criteria;
    }


    private NameInfo createNewNameInfo(String name) {
        return NameInfo.builder()
                .name(name)
                .build();
    }

    private Filter createNewFilter(String name) {
        return Filter.builder()
                .id(1)
                .name(name)
                .build();
    }
}
