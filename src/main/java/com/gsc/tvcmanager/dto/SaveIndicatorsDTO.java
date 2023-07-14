package com.gsc.tvcmanager.dto;


import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveIndicatorsDTO {

    private String id;
    private String oidDealer;
    private String year;
    private String month;
    private String status;
    private String totUsedCars;
    List<SalesLinesDTO> salesLinesList;
}
