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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "FILTER_CRITERIA")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterCriteria {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "FILTER_ID", nullable = false)
    private Filter filter;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CRITERIA_ID", nullable = false)
    private Criteria criteria;
}
