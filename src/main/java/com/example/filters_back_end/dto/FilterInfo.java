package com.example.filters_back_end.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class FilterInfo {

    @JsonIgnore
    private Integer filterId;
    private Integer seqNr;
    private String name;
    private List<CriteriaInfo> criteriaInfos;
}
