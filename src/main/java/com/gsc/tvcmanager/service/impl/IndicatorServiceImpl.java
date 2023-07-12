package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.repository.toyota.IndicatorRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.rg.dealer.Dealer;
import com.rg.objects.DealerCode;
import com.sc.commons.utils.DateTimerTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

@Service
@RequiredArgsConstructor
@Log4j
public class IndicatorServiceImpl implements IndicatorService {


    public static final String STATUS_OPEN		= "Aberto";
    public static final String STATUS_CLOSED	= "Fechado";
    public static final String STATUS_REOPEN	= "Reaberto";
    private final DealerUtils dealerUtils;

    private final PrevisionFilterBean filterBean;

    private final IndicatorRepository indicatorRepository;

    @Override
    public void getIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer) {
        try {
            TVCUsedCarsIndicatorsSales oUsedCarsIndicatorsSales;
            boolean notHasEuroLinea = false;
            List<Dealer> vecDealers = null;
            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());
            } else {
                vecDealers.add(dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer));
            }
            Dealer oDealer;
            oDealer = dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer);
            if (oDealer != null) {
                List<DealerCode> vecCodes = oDealer.getCodes(Dealer.OID_NET_TOYOTA.equals(userPrincipal.getOidNet()) ? Dealer.OID_TOYOTA_CODE_DEKRA : Dealer.OID_LEXUS_CODE_DEKRA);
                if (vecCodes == null || vecCodes.isEmpty())
                    notHasEuroLinea = true;
            }
            boolean isValidOpenMonth = false;
            int actualYear = DateTimerTasks.getCurYear();
            int actualMonth = DateTimerTasks.getCurMonth();
            oUsedCarsIndicatorsSales = indicatorRepository.getUsedCarsIndicatorsSales(1).get();
            if (oUsedCarsIndicatorsSales.getStatus().equals(STATUS_OPEN)) {
                if ((oUsedCarsIndicatorsSales.getYear() == actualYear && oUsedCarsIndicatorsSales.getMonth() == actualMonth - 1) || (oUsedCarsIndicatorsSales.getYear() == actualYear - 1 && oUsedCarsIndicatorsSales.getMonth() == actualMonth + 11)) {
                    isValidOpenMonth = true;
                }
            } else if (oUsedCarsIndicatorsSales == null) {
                if ((filterBean.getYear() == actualYear && filterBean.getMonth() == actualMonth - 1) || (filterBean.getYear() == actualYear - 1 && filterBean.getMonth() == actualMonth + 11)) {
                    oUsedCarsIndicatorsSales = TVCUsedCarsIndicatorsSales.builder().year(filterBean.getYear()).month(filterBean.getMonth()).build();
                    isValidOpenMonth = true;
                }
            }
            if (oUsedCarsIndicatorsSales != null && oUsedCarsIndicatorsSales.getStatus().equals(STATUS_REOPEN)) {
                isValidOpenMonth = true;
            }
            filterBean.setValidOpenMonth(isValidOpenMonth);
            UsedCarsIndicatorDTO.builder()
                    .existInBD(oUsedCarsIndicatorsSales != null ? true : false)
                    .notHasEuroLinea(notHasEuroLinea)
                    .usedCarsIndicatorsSales(oUsedCarsIndicatorsSales)
                    .dealers(vecDealers)
                    .build();
        }catch (Exception e){
            log.error("Visualizar Plano de Indicadores de Venda Usados\",\"Erro ao visualizar formul�rio do Plano de Indicadores de Venda Usados");
            throw  new SalesException("Erro ao visualizar formul�rio do Plano de Indicadores de Venda Usados", e);
        }


    }
}
