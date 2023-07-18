package com.gsc.tvcmanager.service;


import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;

public interface PrevisionService{

    UsedCarsPrevisionDTO getUsedCarsAllPrevisionSalesMonthOrYear(UserPrincipal userPrincipal, Integer year, Integer month,boolean isMonth);
    void saveUsedCarsPrevisionSales(UserPrincipal userPrincipal,int id,String oidDealer,Integer previsionTvc,Integer previsionSn,Integer actualMonth,Integer actualYear,String status);
}
