package com.gsc.tvcmanager.sample.data.provider;


import com.gsc.tvcmanager.dto.PrevisionHtDTO;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.rg.dealer.Dealer;

import java.util.ArrayList;
import java.util.List;

public class TVCData {


    public static List<Dealer> getDealers() {
        List<Dealer> dealers = new ArrayList<>();

        Dealer dealer1 = new Dealer();
        dealer1.setDealerCode("1");
        dealer1.setResp("1");
        dealer1.setCp4(1);
        dealer1.setEmail("a");
        dealer1.setDistrict("d");
        dealer1.setCp3(1);
        dealer1.setObjectId("1");

        dealers.add(dealer1);

        return dealers;
    }

    public static List<TVCUsedCarsPrevisionSales> getMonthPrevisionSales(){
        List<TVCUsedCarsPrevisionSales> previsionSales = new ArrayList<>();

        TVCUsedCarsPrevisionSales previsionSales1 = TVCUsedCarsPrevisionSales.builder()
                .previsionSn(1)
                .previsionTvc(1)
                .month(1)
                .oidDealer("1")
                .id(1)
                .build();

        TVCUsedCarsPrevisionSales previsionSales2 = TVCUsedCarsPrevisionSales.builder()
                .previsionSn(2)
                .previsionTvc(2)
                .month(13)
                .oidDealer("2")
                .id(2)
                .build();

        previsionSales.add(previsionSales1);
        previsionSales.add(previsionSales2);

        return previsionSales;
    }

    public static List<PrevisionHtDTO> getAllPrevisionSales() {
        List<PrevisionHtDTO> previsionHtDTOS = new ArrayList<>();

        PrevisionHtDTO previsionHtDTO1 = PrevisionHtDTO.builder()
                .previsionSn(10L)
                .previsionTvc(20L)
                .oidDealer("1")
                .build();

        previsionHtDTOS.add(previsionHtDTO1);
        return previsionHtDTOS;
    }


}
