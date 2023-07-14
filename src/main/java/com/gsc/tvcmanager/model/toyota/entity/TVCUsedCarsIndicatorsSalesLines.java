package com.gsc.tvcmanager.model.toyota.entity;


import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TVC_USED_CARS_INDICATORS_SALES_LINES")
public class TVCUsedCarsIndicatorsSalesLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;

    @Column(name = "ID_USED_CARS_INDICATORS_SALES")
    private Integer idUsedCarsIndicatorsSales;

    @Column(name = "USED_CARS_INDICATORS")
    private String usedCarsIndicators;

    @Column(name = "INDICATOR_VALUE")
    private Double indicatorValue;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

}
