package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.config.ApplicationConfiguration;
import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
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
    private final PrevisionFilterBean filterBean;
    private final DealerUtils dealerUtils;

    private final PrevisionRepository previsionRepository;


    @Override
    public UsedCarsPrevisionDTO getUsedCarsAllPrevisionSalesMonth(UserPrincipal userPrincipal) {
        List<Dealer> vecDealers = null;
        try {
            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());
            }
            List<TVCUsedCarsPrevisionSales> hstMonthPrevision = usedCarsPrevision(filterBean.getYear(), filterBean.getMonth(), MONTHLY, TVCUsedCarsPrevisionSales.PREVISION_TYPE_MENSAL);
            List<TVCUsedCarsPrevisionSales> hstMonthPrevisionTcap = usedCarsPrevision(filterBean.getYear(), filterBean.getMonth(), MONTHLY, TVCUsedCarsPrevisionSales.PREVISION_TYPE_ANUAL);
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
    public void saveUsedCarsPrevisionSales(UserPrincipal userPrincipal, int id) {
        int idt = StringTasks.cleanInteger(String.valueOf(id), 0);
        TVCUsedCarsPrevisionSales oUsedCarsPrevisionSales;
        try {
            int previsionTvc = StringTasks.cleanInteger("previsionTVC"+filterBean.getActualMonth(), 0);
            int previsionSn = StringTasks.cleanInteger("previsionSN"+filterBean.getActualMonth(), 0);
            String status = StringTasks.cleanString(STATUS, "Aberto");
            if(idt==0){
                oUsedCarsPrevisionSales = TVCUsedCarsPrevisionSales
                        .builder()
                        .oidDealer(filterBean.getOidDealer())
                        .month(filterBean.getActualMonth())
                        .year(filterBean.getActualYear())
                        .previsionType(TVCUsedCarsPrevisionSales.PREVISION_TYPE_MENSAL)
                        .build();
            }else{
                oUsedCarsPrevisionSales= previsionRepository.findById(id).get();
                oUsedCarsPrevisionSales.setChangedBy(userPrincipal.getOidNet());
            }
            oUsedCarsPrevisionSales.setStatus(status);
            oUsedCarsPrevisionSales.setPrevisionTvc(previsionTvc);
            oUsedCarsPrevisionSales.setPrevisionSn(previsionSn);
            previsionRepository.mergeUsedCarsPrevisionSales(oUsedCarsPrevisionSales,"11/07/2023");
        } catch (Exception e) {
            log.error("Guardar Previs�o de vendas usados),Erro ao guardar formul�rio de Previs�o de vendas usados");
        }
    }

    private List<TVCUsedCarsPrevisionSales> usedCarsPrevision(Integer year, Integer month, String reportType, String previsionType){
        TVCUsedCarsPrevisionSales oUsedCarsPrevisionSales = null;
        List<TVCUsedCarsPrevisionSales> usedCars;
        List<TVCUsedCarsPrevisionSales> hstUsedCarsPrevisionSales = new ArrayList<>();
        usedCars = ANNUAL.equals(reportType)
                     ? previsionRepository.previsionAnnual(year, month, reportType, previsionType)
                     : otherPrevision(year, month, reportType, previsionType);
        for(TVCUsedCarsPrevisionSales data : usedCars){
            if(ANNUAL.equals(reportType)){
                oUsedCarsPrevisionSales = TVCUsedCarsPrevisionSales.builder()
                        .oidDealer(data.getOidDealer())
                        .previsionTvc(data.getPrevisionTvc())
                        .previsionSn(data.getPrevisionSn())
                        .build();
            }else{
                //oUsedCarsPrevisionSales = (UsedCarsPrevisionSales)rowToObject(rs);
            }
            hstUsedCarsPrevisionSales.add(oUsedCarsPrevisionSales);
        }
        return hstUsedCarsPrevisionSales;
    }

    private List<TVCUsedCarsPrevisionSales> otherPrevision(Integer year,Integer month,String reportType,String previsionType){
       return EXCEL.equals(reportType)
                ? previsionRepository.previsionExcell(year, month, reportType, previsionType)
                : previsionRepository.previsionMonthly(year, month, reportType, previsionType);
    }
}
