package com.example.filters_back_end.mappers;

import com.example.filters_back_end.dto.CriteriaInfo;
import com.example.filters_back_end.entities.Criteria;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CriteriaMapper {

    @Mapping(target = "type", source = "criteria.criteriaTypeCc.criteriaType.name")
    @Mapping(target = "condition", source = "criteria.criteriaTypeCc.comparingCondition.name")
    @Mapping(ignore = true, target = "value")
    CriteriaInfo criteriaToCriteriaInfo(Criteria criteria);

    List<CriteriaInfo> criteriasToCriteriaInfos(List<CriteriaInfo> criterias);
}
