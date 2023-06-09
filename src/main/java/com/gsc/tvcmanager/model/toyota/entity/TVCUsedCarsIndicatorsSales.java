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
@Table(name = "TVC_USED_CARS_INDICATORS_SALES")
public class TVCUsedCarsIndicatorsSales {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "MONTH")
    private Integer month;

    @Column(name = "TOT_USED_CARS")
    private Integer totUsedCars;

    @Column(name = "STATUS")
    private String status = "Aberto";
    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
}
