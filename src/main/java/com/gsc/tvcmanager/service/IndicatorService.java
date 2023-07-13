package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface IndicatorService {

    IndicatorUsedFilesDTO getIndicatorsUsedFilesList(UserPrincipal userPrincipal, String oidDealer, Integer year, String uploadDir);
    IndicatorUsedFilesDTO saveIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String idR,String oidDealer,
                                                      String yearR, String monthR,String uploadDir, String status, String totUsedCars);
    void getIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer);
}
