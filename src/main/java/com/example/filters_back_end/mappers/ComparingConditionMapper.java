package com.example.filters_back_end.mappers;

import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.ComparingCondition;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface ComparingConditionMapper {

    NameInfo comparingConditionToNameInfo(ComparingCondition comparingCondition);

    List<NameInfo> comparingConditionsToNameInfos(List<ComparingCondition> comparingConditions);
}
