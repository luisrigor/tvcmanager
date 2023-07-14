package com.gsc.tvcmanager.dto;


import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.rg.dealer.Dealer;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsedCarsIndicatorDTO {

    private boolean existInBD;
    private boolean isCa;
    private boolean notHasEuroLinea;

    private TVCUsedCarsIndicatorsSales usedCarsIndicatorsSales;

    private List<Dealer> dealers;

    private boolean isCA;

    private boolean filterBeanisValidOpenMonth;
}
