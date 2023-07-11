package com.gsc.tvcmanager.dto;

import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.rg.dealer.Dealer;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedCarsPrevisionDTO {

    List<Dealer> vecDealers;
    List<TVCUsedCarsPrevisionSales> hstUsedCarsPrevisionSales;
    List<TVCUsedCarsPrevisionSales> hstAnualPrevision;
}
