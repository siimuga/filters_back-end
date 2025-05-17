package com.example.filters_back_end;

import com.example.filters_back_end.dto.FilterInfo;
import com.example.filters_back_end.dto.FilterRequest;
import com.example.filters_back_end.dto.NameInfo;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@CrossOrigin("*")
@RestController
@Validated
@RequestMapping("api")
public class FilterController {

    @Resource
    FilterServiceI filterServiceI;

    @GetMapping("criteriaType")
    public List<NameInfo> findAllCriteriaTypes() {
        return filterServiceI.findAllCriteriaTypes();
    }

    @GetMapping("comparingCondition")
    public List<NameInfo> findAllComparingConditionsByType(@RequestParam @NotBlank(message = "type cannot be blank") String type) {
        return filterServiceI.findAllComparingConditionsByType(type);
    }

    @GetMapping("filters")
    public List<FilterInfo> findAllFiltersWithCriterias() {
        return filterServiceI.findAllFiltersWithCriterias();
    }

    @PostMapping("filter")
    public ResponseEntity<?> addFilter(@RequestBody @Valid FilterRequest request) {
        filterServiceI.addFilter(request);
        Map<String, String> result = new HashMap<>();
        result.put("message", "Filter added successfully");
        return new ResponseEntity<>(result, OK);
    }

}
