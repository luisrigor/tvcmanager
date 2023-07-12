package com.gsc.tvcmanager.model.toyota.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TVC_USED_CARS_PREVISION_SALES")
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

    @Column(name = "STATUS")
    private String status = "Aberto";
}
