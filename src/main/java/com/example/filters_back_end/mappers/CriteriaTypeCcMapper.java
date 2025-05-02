package com.example.filters_back_end.mappers;

import com.example.filters_back_end.dto.NameInfo;
import com.example.filters_back_end.entities.CriteriaTypeCc;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring")
public interface CriteriaTypeCcMapper {

    @Mapping(target = "name", source = "ctc.comparingCondition.name")
    NameInfo criteriaTypeCcToNameInfo(CriteriaTypeCc ctc);

    List<NameInfo> criteriaTypeCcsToNameInfos(List<CriteriaTypeCc> ctc);
}
