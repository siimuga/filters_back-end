package com.example.filters_back_end;

import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.ComparingCondition;
import com.example.filters_back_end.entities.CriteriaType;
import com.example.filters_back_end.mappers.ComparingConditionMapper;
import com.example.filters_back_end.mappers.CriteriaTypeMapper;
import com.example.filters_back_end.repos.ComparingConditionRepository;
import com.example.filters_back_end.repos.CriteriaTypeRepository;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FilterService {

    @Resource
    private CriteriaTypeRepository criteriaTypeRepository;

    @Resource
    private ComparingConditionRepository comparingConditionRepository;

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
}
