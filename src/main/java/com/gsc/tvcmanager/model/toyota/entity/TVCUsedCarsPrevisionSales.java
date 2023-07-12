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
@Table(name = "TVC_USED_CARS_PREVISION_SALES")
public class TVCUsedCarsPrevisionSales {

    public static final String STATUS_OPEN			= "Aberto";
    public static final String STATUS_CLOSE			= "Fechado";
    public static final String PREVISION_TYPE_ANUAL	= "Anual";
    public static final String PREVISION_TYPE_MENSAL= "Mensal";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "OID_DEALER")
    private String oidDealer;
    @Column(name = "YEAR")
    private Integer year;
    @Column(name = "MONTH")
    private Integer month;
    @Column(name = "PREVISION_TYPE")
    private String previsionType = "Mensal";

    @Column(name = "STATUS")
    private String status = "Aberto";

    @Column(name = "PREVISION_TVC")
    private Integer previsionTvc;

    @Column(name = "PREVISION_SN")
    private Integer previsionSn;

    @Column(name = "CREATED_BY")
    private String createdBy;

    @Column(name = "DT_CREATED")
    private LocalDateTime dtCreated;

    @Column(name = "CHANGED_BY")
    private String changedBy;

    @Column(name = "DT_CHANGED")
    private LocalDateTime dtChanged;

}
