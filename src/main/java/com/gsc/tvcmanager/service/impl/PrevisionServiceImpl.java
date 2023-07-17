package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.gsc.tvcmanager.repository.toyota.PrevisionRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.PrevisionService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@RequiredArgsConstructor
@Log4j
public class PrevisionServiceImpl implements PrevisionService {

    public static final String ANNUAL = "ANUAL";
    public static final String MONTHLY = "MENSAL";
    public static final String EXCEL = "EXCELL";
    public static final String STATUS = "status";
    private final DealerUtils dealerUtils;

    private final PrevisionRepository previsionRepository;


    @Override
    public UsedCarsPrevisionDTO getUsedCarsAllPrevisionSalesMonthOrYear(UserPrincipal userPrincipal, Integer year, Integer month, boolean isMonth) {
        List<Dealer> vecDealers = null;
        try {
            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());
            }
            List<TVCUsedCarsPrevisionSales> hstMonthPrevision = usedCarsPrevision(year, month, isMonth ? MONTHLY : ANNUAL, TVCUsedCarsPrevisionSales.PREVISION_TYPE_MENSAL);
            List<TVCUsedCarsPrevisionSales> hstMonthPrevisionTcap = usedCarsPrevision(year, month, isMonth ? MONTHLY : ANNUAL, TVCUsedCarsPrevisionSales.PREVISION_TYPE_ANUAL);
            return UsedCarsPrevisionDTO.builder()
                    .vecDealers(vecDealers)
                    .hstUsedCarsPrevisionSales(hstMonthPrevision)
                    .hstAnualPrevision(hstMonthPrevisionTcap)
                    .build();
        } catch (Exception e) {
            log.error("Visualizar Previs�o de Vendas de Usados Erro ao visualizar formul�rio de Previs�o de Vendas de Usados");
            throw new RuntimeException("Erro ao visualizar formul�rio de Previs�o de Vendas de Usados");
        }
    }

    @Override
    public void saveUsedCarsPrevisionSales(UserPrincipal userPrincipal, int id,String oidDealer,Integer actualMonth,Integer actualYear,String status) {
        int idt = StringTasks.cleanInteger(String.valueOf(id), 0);
        TVCUsedCarsPrevisionSales oUsedCarsPrevisionSales;
        try {
            int previsionTvc = StringTasks.cleanInteger("previsionTVC"+actualMonth, 0);
           int previsionSn = StringTasks.cleanInteger("previsionSN"+actualMonth, 0);
           String statusCar = StringTasks.cleanString(status, "Aberto");
            String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];
            if(idt==0){
                oUsedCarsPrevisionSales = TVCUsedCarsPrevisionSales
                        .builder()
                       .oidDealer(oidDealer)
                       .month(actualMonth)
                       .year(actualYear)
                        .previsionType(TVCUsedCarsPrevisionSales.PREVISION_TYPE_MENSAL)
                        .build();
            }else{
                oUsedCarsPrevisionSales= previsionRepository.findById(id).get();
                oUsedCarsPrevisionSales.setChangedBy(userStamp);
            }
            oUsedCarsPrevisionSales.setStatus(statusCar);
            oUsedCarsPrevisionSales.setPrevisionTvc(previsionTvc);
            oUsedCarsPrevisionSales.setPrevisionSn(previsionSn);
            previsionRepository.mergeUsedCarsPrevisionSales(oUsedCarsPrevisionSales,userStamp);
        } catch (Exception e) {
            log.error("Guardar Previs�o de vendas usados),Erro ao guardar formul�rio de Previs�o de vendas usados");
        }
    }

    private List<TVCUsedCarsPrevisionSales> usedCarsPrevision(Integer year, Integer month, String reportType, String previsionType){
        return otherPrevision(year, month, reportType, previsionType);
    }

    private List<TVCUsedCarsPrevisionSales> otherPrevision(Integer year,Integer month,String reportType,String previsionType){
       return EXCEL.equals(reportType)
                ? previsionRepository.previsionExcell(year, month,  previsionType)
                : previsionRepository.previsionMonthly(year, month, previsionType);
    }
}
