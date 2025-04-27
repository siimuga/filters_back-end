package com.example.filters_back_end.mappers;

import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.CriteriaType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CriteriaTypeMapper {

    NameInfo criteriaTypeToNameInfo(CriteriaType type);

    List<NameInfo> criteriaTypesToNameInfos(List<CriteriaType> types);
}
