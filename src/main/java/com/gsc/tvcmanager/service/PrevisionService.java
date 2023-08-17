package com.gsc.tvcmanager.service;


import com.gsc.tvcmanager.dto.SavePrevisionDTO;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface PrevisionService{

    UsedCarsPrevisionDTO getUsedCarsAllPrevisionSalesMonthOrYear(UserPrincipal userPrincipal, Integer year, Integer month,boolean isMonth);
    void saveUsedCarsPrevisionSales(UserPrincipal userPrincipal, SavePrevisionDTO savePrevisionDTO);
}
