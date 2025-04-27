package com.example.filters_back_end.mappers;

import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.entities.FilterCriteria;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface FilterCriteriaMapper {

    FilterInfo filterToFilterInfo(FilterCriteria filter);

    List<FilterInfo> filtersToFilterInfos(List<FilterCriteria> types);
}
