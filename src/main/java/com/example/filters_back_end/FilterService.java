package com.example.filters_back_end;

import com.example.filters_back_end.dto.CriteriaInfo;
import com.example.filters_back_end.dto.CriteriaRequest;
import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.ComparingCondition;
import com.example.filters_back_end.entities.Criteria;
import com.example.filters_back_end.entities.CriteriaType;
import com.example.filters_back_end.entities.CriteriaTypeCc;
import com.example.filters_back_end.entities.Filter;
import com.example.filters_back_end.entities.FilterCriteria;
import com.example.filters_back_end.mappers.ComparingConditionMapper;
import com.example.filters_back_end.mappers.CriteriaMapper;
import com.example.filters_back_end.mappers.CriteriaTypeMapper;
import com.example.filters_back_end.repos.ComparingConditionRepository;
import com.example.filters_back_end.repos.CriteriaRepository;
import com.example.filters_back_end.repos.CriteriaTypeCcRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import com.example.filters_back_end.repos.FilterCriteriaRepository;
import com.example.filters_back_end.repos.FilterRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.example.filters_back_end.enums.CriteriaTypeEnum.AMOUNT;
import static com.example.filters_back_end.enums.CriteriaTypeEnum.TITLE;

@Service
public class FilterService {

    @Resource
    private FilterCriteriaRepository filterCriteriaRepository;

    @Resource
    private CriteriaTypeRepository criteriaTypeRepository;

    @Resource
    private CriteriaTypeCcRepository criteriaTypeCcRepository;

    @Resource
    private ComparingConditionRepository comparingConditionRepository;

    @Resource
    private FilterRepository filterRepository;

    @Resource
    private CriteriaRepository criteriaRepository;

    @Resource
    private CriteriaMapper criteriaMapper;

    @Resource
    private CriteriaTypeMapper criteriaTypeMapper;

    @Resource
    private ComparingConditionMapper comparingConditionMapper;

    public List<NameInfo> findAllCriteriaTypes() {
        List<CriteriaType> criteriaTypes = criteriaTypeRepository.findAll();
        return criteriaTypeMapper.criteriaTypesToNameInfos(criteriaTypes);
    }

    public List<NameInfo> findAllComparingConditions() {
        List<ComparingCondition> comparingConditions = comparingConditionRepository.findAll();
        return comparingConditionMapper.comparingConditionsToNameInfos(comparingConditions);
    }

    public List<FilterInfo> findAllFiltersWithCriterias() {
        List<FilterInfo> filterInfos = new ArrayList<>();
        List<FilterCriteria> filterWithCriterias = filterCriteriaRepository.findAll();

        for (FilterCriteria filterCriteria : filterWithCriterias) {
            boolean isFilterExisting = filterInfos.stream()
                    .anyMatch(filterInfo -> filterInfo.getFilterId().equals(filterCriteria.getFilter().getId()));
            fillFilterInfos(filterCriteria, isFilterExisting, filterInfos);
        }

        setSeqNumbers(filterInfos);
        return filterInfos;
    }

    @Transactional
    public void addFilter(FilterRequest request) {
        Filter filter = createNewFilter(request);
        filter = filterRepository.save(filter);
        List<Criteria> criterias = createNewCriterias(request.getCriteriaRequests());
        criteriaRepository.saveAll(criterias);
        List<FilterCriteria> filterCriterias = createNewFilterCriterias(criterias, filter);
        filterCriteriaRepository.saveAll(filterCriterias);
    }

    private List<FilterCriteria> createNewFilterCriterias(List<Criteria> criterias, Filter filter) {
        List<FilterCriteria> filterCriterias = new ArrayList<>();
        for (Criteria criteria : criterias) {
            FilterCriteria filterCriteria = createNewFilterCriteria(criteria, filter);
            filterCriterias.add(filterCriteria);
        }
        return filterCriterias;
    }

    private FilterCriteria createNewFilterCriteria(Criteria criteria, Filter filter) {
        return FilterCriteria.builder()
                .filter(filter)
                .criteria(criteria)
                .build();
    }

    private Filter createNewFilter(FilterRequest request) {
        return Filter.builder()
                .name(request.getName())
                .build();
    }

    private List<Criteria> createNewCriterias(List<CriteriaRequest> requests) {
        List<Criteria> criterias = new ArrayList<>();
        for (CriteriaRequest request : requests) {
            criterias.add(createNewCriteria(request));
        }
        return criterias;
    }

    private Criteria createNewCriteria(CriteriaRequest request) {
        Criteria criteria = new Criteria();
        CriteriaTypeCc criteriaTypeCc = criteriaTypeCcRepository.findByComparingConditionAndCriteriaType(request.getCondition(), request.getType());
        handleValueSelection(request, criteria);
        criteria.setCriteriaTypeCc(criteriaTypeCc);
        return criteria;
    }

    private static void handleValueSelection(CriteriaRequest request, Criteria criteria) {
        if (TITLE.getTextValue().equals(request.getType())){
            criteria.setTitle(request.getValue());
        } else if (AMOUNT.getTextValue().equals(request.getType())) {
            criteria.setAmount(Integer.valueOf(request.getValue()));
        } else {
            SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
            try {
                Date date = formatter.parse(request.getValue());
                criteria.setDate(date);
            } catch (ParseException e) {
                throw new RuntimeException();
            }
        }
    }

    private void fillFilterInfos(FilterCriteria filterCriteria, boolean isFilterExisting, List<FilterInfo> filterInfos) {
        if (!isFilterExisting) {
            FilterInfo filterInfo = createNewMappedFilterInfo(filterCriteria);
            filterInfos.add(filterInfo);
        } else {
            Optional<FilterInfo> filterInfo = filterInfos.stream()
                    .filter(fi -> fi.getFilterId().equals(filterCriteria.getFilter().getId()))
                    .findFirst();
            if (filterInfo.isPresent()) {
                CriteriaInfo criteriaInfo = criteriaMapper.criteriaToCriteriaInfo(filterCriteria.getCriteria());
                criteriaInfo.setValue(getCriteriaInfoValue(filterCriteria.getCriteria()));
                filterInfo.get().getCriteriaInfos().add(criteriaInfo);
            }
        }
    }

    private FilterInfo createNewMappedFilterInfo(FilterCriteria filterCriteria) {
        FilterInfo filterInfo = new FilterInfo();
        CriteriaInfo criteriaInfo = criteriaMapper.criteriaToCriteriaInfo(filterCriteria.getCriteria());
        criteriaInfo.setValue(getCriteriaInfoValue(filterCriteria.getCriteria()));
        List<CriteriaInfo> criteriaInfos = new ArrayList<>();
        criteriaInfos.add(criteriaInfo);
        filterInfo.setCriteriaInfos(criteriaInfos);
        filterInfo.setFilterId(filterCriteria.getFilter().getId());
        filterInfo.setName(filterCriteria.getFilter().getName());
        return filterInfo;
    }

    private static void setSeqNumbers(List<FilterInfo> filterInfos) {
        for (FilterInfo filterInfo : filterInfos) {
            filterInfo.setSeqNr(filterInfos.indexOf(filterInfo) + 1);
            List<CriteriaInfo> criteriaInfos = filterInfo.getCriteriaInfos();
            for (CriteriaInfo criteriaInfo : criteriaInfos) {
                criteriaInfo.setSeqNr(criteriaInfos.indexOf(criteriaInfo) + 1);
            }
        }
    }

    private static String getCriteriaInfoValue(Criteria criteria) {
        if (criteria.getAmount() != null) {
            return String.valueOf(criteria.getAmount());
        }
        if (criteria.getTitle() != null) {
            return criteria.getTitle();
        }
        return DateFormatUtils.format(criteria.getDate(), "dd.MM.yyyy");
    }
}
