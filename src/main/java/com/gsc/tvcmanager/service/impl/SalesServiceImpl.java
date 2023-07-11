package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsPrevisionSalesRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.SalesService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.rg.dealer.Dealer;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class SalesServiceImpl implements SalesService {

    private final TVCUsedCarsPrevisionSalesRepository tvcUsedCarsPrevisionSalesRepository;
    private final DealerUtils dealerUtils;

    public static final String STATUS_OPEN			= "Aberto";
    public static final String STATUS_CLOSE			= "Fechado";
    public static final String PREVISION_TYPE_ANUAL	= "Anual";
    public static final String PREVISION_TYPE_MENSAL= "Mensal";

    @Override
    public UsedCarsPrevisionDTO getUsedCarsPrevisionSales(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month) {
        List<Dealer> vecDealers = new ArrayList<>();
        List<TVCUsedCarsPrevisionSales> hstUsedCarsPrevisionSales = new ArrayList<>();
        List<TVCUsedCarsPrevisionSales> hstAnualPrevision = new ArrayList<>();
        try {

            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());
            } else {
                vecDealers.add(dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer));
            }
            hstUsedCarsPrevisionSales = tvcUsedCarsPrevisionSalesRepository.getMonthPrevisionByDealerHt(oidDealer, year, month, PREVISION_TYPE_MENSAL);
            verifyHtMonths(hstUsedCarsPrevisionSales, year, oidDealer);
            hstAnualPrevision = tvcUsedCarsPrevisionSalesRepository.getMonthPrevisionByDealerHt(oidDealer, year, month, PREVISION_TYPE_ANUAL);
        } catch (Exception e) {
            log.error("Visualizar Previsão de Vendas de Usados\",\"Erro ao visualizar formulário de Previsão de Vendas de Usado");
            throw  new SalesException("Erro ao visualizar formulário de Previsão de Vendas de Usado", e);
        }

        return UsedCarsPrevisionDTO.builder()
                .hstUsedCarsPrevisionSales(hstUsedCarsPrevisionSales)
                .vecDealers(vecDealers)
                .hstAnualPrevision(hstAnualPrevision)
                .build();
    }

    @Override
    public UsedCarsPrevisionDTO getYearReport(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month) {
        return null;
    }

    private void verifyHtMonths(List<TVCUsedCarsPrevisionSales> hstUsedCarsPrevisionSales, Integer year, String oidDealer) {
        List<Integer> months = Arrays.asList(1,2,3,4,5,6,7,8,9,10,11,12);

        TVCUsedCarsPrevisionSales oUsedCarsPrevisionSales = null;
        for (int i=0;i<hstUsedCarsPrevisionSales.size(); i++) {
            if (!months.contains(hstUsedCarsPrevisionSales.get(i).getMonth())) {
                oUsedCarsPrevisionSales = new TVCUsedCarsPrevisionSales();
                oUsedCarsPrevisionSales.setMonth(hstUsedCarsPrevisionSales.get(i).getMonth());
                oUsedCarsPrevisionSales.setYear(year);
                oUsedCarsPrevisionSales.setOidDealer(oidDealer);
                oUsedCarsPrevisionSales.setPrevisionType(PREVISION_TYPE_MENSAL);
                oUsedCarsPrevisionSales.setPrevisionTvc(-1);
                oUsedCarsPrevisionSales.setPrevisionSn(-1);
                oUsedCarsPrevisionSales.setStatus(STATUS_OPEN);
                hstUsedCarsPrevisionSales.set(i, oUsedCarsPrevisionSales);
            }
        }
    }
}
