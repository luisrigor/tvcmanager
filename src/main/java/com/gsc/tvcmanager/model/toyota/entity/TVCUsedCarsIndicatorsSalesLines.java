package com.gsc.tvcmanager.model.toyota.entity;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TVC_USED_CARS_INDICATORS_SALES_LINES")
public class TVCUsedCarsIndicatorsSalesLines {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;



    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;
}
