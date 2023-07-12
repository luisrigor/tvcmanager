package com.gsc.tvcmanager.dto;

import com.rg.dealer.Dealer;
import lombok.*;

import java.io.File;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class IndicatorUsedFilesDTO {

    List<Dealer> vecDealers;
    List<File> vecFilesList;
}
