package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.exceptions.IndicatorClientException;
import com.gsc.tvcmanager.exceptions.IndicatorsException;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.repository.toyota.IndicatorRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.FileTasks;
import com.sc.commons.utils.PortletTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Enumeration;
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

    private final DealerUtils dealerUtils;
    public static final String STATUS_OPEN		= "Aberto";
    public static final String STATUS_CLOSED	= "Fechado";
    public static final String STATUS_REOPEN	= "Reaberto";
    private final PrevisionFilterBean filterBean;
    private final IndicatorRepository indicatorRepository;

    @Override
    public IndicatorUsedFilesDTO getIndicatorsUsedFilesList(UserPrincipal userPrincipal, String oidDealer, Integer year, String uploadDir) {
        List<Dealer> vecDealers = new ArrayList<>();
        List<File> vecFilesList = new ArrayList<>();
        try {
            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS)) {
                vecDealers = dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());
                vecDealers.add(dealerUtils.getByObjectIdOI(userPrincipal.getOidNet(), Dealer.OID_NMSC));
            } else {
                vecDealers.add(dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer));
            }

            if (!StringUtils.isEmpty(oidDealer)) {
                Dealer oDealer = null;
                oDealer = dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer);

                if (oDealer != null && oDealer.getDealerCode() != null && oDealer.getSalesCode() != null) {
                    String brandCode = (Dealer.OID_NET_LEXUS.equals(userPrincipal.getOidNet())) ? "L" : "T";
                    String strToSearch = brandCode + "_" + oDealer.getDealerCode();
                    if (year > 0)
                        strToSearch = strToSearch + "_" + year;

                    log.trace("uploadDir=" + uploadDir);
                    log.trace("strToSearch=" + strToSearch);

                    vecFilesList = FileTasks.getFiles(uploadDir, strToSearch);
                }
            }

            return IndicatorUsedFilesDTO.builder()
                    .vecDealers(vecDealers)
                    .vecFilesList(vecFilesList)
                    .build();
        } catch (Exception e) {
            log.error("Erro ao visualizar Lista de relatórios de Indicadores de Venda Usados", e);
            throw new IndicatorsException("Erro ao visualizar Lista de relatórios de Indicadores de Venda Usados", e);
        }
    }

    @Override
    public IndicatorUsedFilesDTO saveIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer, Integer year, String uploadDir) {
        int id = StringTasks.cleanInteger(request.getParameter("id"), 0);
        String oidNet = oGSCUser.getOidNet();
        String oidDealer = StringTasks.cleanString(request.getParameter("oidDealer"), oGSCUser.getOidDealerParent());
        String status = StringTasks.cleanString(request.getParameter(STATUS), UsedCarsIndicatorsSales.STATUS_OPEN);
        int year = StringTasks.cleanInteger(request.getParameter("year"), DateTimerTasks.getCurYear());
        int month = StringTasks.cleanInteger(request.getParameter("month"), DateTimerTasks.getCurMonth());
        UsedCarsIndicatorsSales oUsedCarsIndicatorsSales = null;

        if (oidNet == null || oidNet.equals("") || oidDealer.equals("")) {
            log.warn("id.................:" + id);
            log.warn("oidNet.............:" + oidNet);
            log.warn("oidDealer...:" + oidDealer);
            throw new IndicatorClientException("Impossóvel guardar informaçõo. Existem parâmetros não definidos \n " +
                    "id.................:" + id + "\n" +, )
            PortletTasks.setMessage(request, "Impossóvel guardar informaçõo. Existem parâmetros não definidos");
            return;
        }

        try {
            if (id == 0) {
                oUsedCarsIndicatorsSales = new UsedCarsIndicatorsSales();
                oUsedCarsIndicatorsSales.setOidDealer(StringTasks.cleanString(request.getParameter("oidDealer"), oGSCUser.getOidDealerParent()));
                oUsedCarsIndicatorsSales.setMonth(month);
                oUsedCarsIndicatorsSales.setYear(year);
            } else {
                oUsedCarsIndicatorsSales = (UsedCarsIndicatorsSales) UsedCarsIndicatorsSales.getHelper().getObjectById(id, ApplicationConfiguration.DATASOURCE_TOYXTAPS);
                oUsedCarsIndicatorsSales.setChangedBy(oGSCUser.getUserStamp());
            }
            if (request.getParameter("TOT_USED_CARS") != null)
                oUsedCarsIndicatorsSales.setTotUsedCars(StringTasks.cleanInteger(request.getParameter("TOT_USED_CARS"), 0));
            oUsedCarsIndicatorsSales.setStatus(status);

            Vector<UsedCarsIndicatorsSalesLines> vecUsedCarsIndicatorsSalesLines = new Vector<UsedCarsIndicatorsSalesLines>();
            UsedCarsIndicatorsSalesLines oUsedCarsIndicatorsSalesLines = null;

            Enumeration<?> enumParams = request.getParameterNames();
            while (enumParams.hasMoreElements()) {
                String paramName = (String) enumParams.nextElement();
                if (paramName.startsWith("USED_CARS_INDICATORS_SALES_LINES_")) {
                    double indicatorValue = StringTasks.cleanDouble(request.getParameter(paramName), -1);
                    String usedCarsIndicators = StringTasks.cleanString(paramName.substring(paramName.lastIndexOf("_") + 1), "-1");
                    oUsedCarsIndicatorsSalesLines = new UsedCarsIndicatorsSalesLines();
                    oUsedCarsIndicatorsSalesLines.setUsedCarsIndicators(usedCarsIndicators);
                    oUsedCarsIndicatorsSalesLines.setIndicatorValue(indicatorValue);
                    oUsedCarsIndicatorsSalesLines.setChangedBy(oGSCUser.getUserStamp());
                    vecUsedCarsIndicatorsSalesLines.add(oUsedCarsIndicatorsSalesLines);
                }
            }
            oUsedCarsIndicatorsSales.setVecUsedCarsIndicatorsSalesLines(vecUsedCarsIndicatorsSalesLines);
            UsedCarsIndicatorsSales.getHelper().mergeUsedCarsIndicatorsSales(oUsedCarsIndicatorsSales, oGSCUser.getUserStamp());
        } catch (Exception e) {
            throw new IndicatorsException("Erro ao guardar formulário do Plano de Indicadores da Rede (Venda Usados)");
        }

    }

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
