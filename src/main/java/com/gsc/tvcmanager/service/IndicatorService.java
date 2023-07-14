package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface IndicatorService {

    IndicatorUsedFilesDTO getIndicatorsUsedFilesList(UserPrincipal userPrincipal, String oidDealer, Integer year, String uploadDir);
    void saveIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, SaveIndicatorsDTO saveIndicatorsDTO);
    UsedCarsIndicatorDTO getIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month);
    PrevisionFilterBean setFilter(UserPrincipal userPrincipal);

}
