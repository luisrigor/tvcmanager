package com.gsc.tvcmanager.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SavePrevisionDTO {

    private Integer id;
    private String oidDealer;
    private Integer previsionTvc;
    private Integer previsionSn;
    private Integer actualMonth;
    private Integer actualYear;
    private String status;
}
