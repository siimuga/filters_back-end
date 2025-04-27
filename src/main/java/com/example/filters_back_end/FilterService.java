package com.example.filters_back_end;

import com.example.filters_back_end.dto.CriteriaInfo;
import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.ComparingCondition;
import com.example.filters_back_end.entities.Criteria;
import com.example.filters_back_end.entities.CriteriaType;
import com.example.filters_back_end.entities.FilterCriteria;
import com.example.filters_back_end.mappers.ComparingConditionMapper;
import com.example.filters_back_end.mappers.CriteriaMapper;
import com.example.filters_back_end.mappers.CriteriaTypeMapper;
import com.example.filters_back_end.repos.ComparingConditionRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import com.example.filters_back_end.repos.FilterCriteriaRepository;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FilterService {

    @Resource
    private FilterCriteriaRepository filterCriteriaRepository;

    @Resource
    private CriteriaTypeRepository criteriaTypeRepository;

    @Resource
    private ComparingConditionRepository comparingConditionRepository;

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
