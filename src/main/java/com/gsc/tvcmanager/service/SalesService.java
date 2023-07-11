package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface SalesService {

    UsedCarsPrevisionDTO getUsedCarsPrevisionSales(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month);
    UsedCarsPrevisionDTO getYearReport(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month);

}
