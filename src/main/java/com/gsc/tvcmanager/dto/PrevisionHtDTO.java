package com.gsc.tvcmanager.dto;


import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrevisionHtDTO {

    private String oidDealer;
    private Long previsionTvc;
    private Long previsionSn;

}
