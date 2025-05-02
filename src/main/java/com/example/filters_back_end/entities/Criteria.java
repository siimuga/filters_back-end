package com.example.filters_back_end.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "CRITERIA")
public class Criteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CRITERIA_TYPE_CC_ID", nullable = false)
    private CriteriaTypeCc criteriaTypeCc;

    @Column(name = "AMOUNT")
    private Integer amount;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "DATE")
    private Date date;
}
