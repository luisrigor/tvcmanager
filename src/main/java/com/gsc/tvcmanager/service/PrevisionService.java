package com.gsc.tvcmanager.service;


import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface PrevisionService{

    UsedCarsPrevisionDTO getUsedCarsAllPrevisionSalesMonth(UserPrincipal userPrincipal, Integer year, Integer month);
    void saveUsedCarsPrevisionSales(UserPrincipal userPrincipal,int id);
}
