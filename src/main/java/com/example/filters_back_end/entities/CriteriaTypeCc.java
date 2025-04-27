package com.example.filters_back_end.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "CRITERIA_TYPE_CC")
public class CriteriaTypeCc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @Column(name = "CRITERIA_TYPE_ID", nullable = false)
    private Integer criteriaTypeId;

    @Column(name = "COMPARING_CONDITION_ID", nullable = false)
    private Integer comparingConditionId;
}
