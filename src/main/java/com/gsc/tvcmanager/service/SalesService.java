package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

import javax.servlet.http.HttpServletResponse;

public interface SalesService {

    UsedCarsPrevisionDTO getUsedCarsPrevisionSales(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month);
    void getReportByYearAndMonth(UserPrincipal userPrincipal, HttpServletResponse response, String oidDealer, Integer year, Integer month, boolean isOnlyYear);

}
