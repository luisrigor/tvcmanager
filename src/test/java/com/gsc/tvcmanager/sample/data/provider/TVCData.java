package com.gsc.tvcmanager.sample.data.provider;


import com.gsc.tvcmanager.dto.PrevisionHtDTO;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.rg.dealer.Dealer;
import com.rg.objects.DealerCode;

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
        dealer1.setSalesCode("s");

        dealers.add(dealer1);

        return dealers;
    }

    public  static List<DealerCode> getDealerCodes() {
        List<DealerCode> dealerCodes = new ArrayList<>();

        DealerCode dealerCode = new DealerCode();
        dealerCode.setOidDealer("1");
        dealerCode.setOidCode("1");
        dealerCode.setDescription("d");
        dealerCode.setValue("v");

        dealerCodes.add(dealerCode);
        return dealerCodes;
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

    public static List<TVCUsedCarsPrevisionSales> getAllPrevisionSalesHT() {
        List<TVCUsedCarsPrevisionSales> previsionHtDTOS = new ArrayList<>();

        TVCUsedCarsPrevisionSales previsionHtDTO1 = TVCUsedCarsPrevisionSales.builder()
                .previsionSn(1)
                .previsionTvc(2)
                .oidDealer("6")
                .build();

        previsionHtDTOS.add(previsionHtDTO1);

        return previsionHtDTOS;
    }

    public  static TVCUsedCarsIndicatorsSales getIndicatorSales() {
        return  TVCUsedCarsIndicatorsSales.builder()
                .id(1)
                .month(6)
                .year(2023)
                .status("Aberto")
                .totUsedCars(1)
                .build();
    }

    public static List<PrevisionHtDTO> getCarsPrevision() {
        List<PrevisionHtDTO> previsionHtDTOS = new ArrayList<>();

        PrevisionHtDTO previsionHtDTO1 = PrevisionHtDTO.builder()
                .previsionSn(1L)
                .oidDealer("1")
                .previsionTvc(1L)
                .build();

        PrevisionHtDTO previsionHtDTO2 = PrevisionHtDTO.builder()
                .previsionSn(2L)
                .oidDealer("2")
                .previsionTvc(2L)
                .build();

        previsionHtDTOS.add(previsionHtDTO1);
        previsionHtDTOS.add(previsionHtDTO2);

        return previsionHtDTOS;
    }

    public  static  List<TVCUsedCarsPrevisionSales> getUsedCarsPrevision(){
        List<TVCUsedCarsPrevisionSales> previsionSalesList = new ArrayList<>();

        TVCUsedCarsPrevisionSales previsionSales1 = TVCUsedCarsPrevisionSales.builder()
                .previsionType("1")
                .previsionSn(1)
                .id(1)
                .previsionTvc(1)
                .month(1)
                .build();

        TVCUsedCarsPrevisionSales previsionSales2 = TVCUsedCarsPrevisionSales.builder()
                .previsionType("1")
                .previsionSn(1)
                .id(1)
                .previsionTvc(1)
                .month(1)
                .build();

        previsionSalesList.add(previsionSales1);
        previsionSalesList.add(previsionSales2);

        return previsionSalesList;
    }


}

