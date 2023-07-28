package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SalesLinesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.exceptions.IndicatorClientException;
import com.gsc.tvcmanager.exceptions.IndicatorsException;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSales;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsIndicatorsSalesLines;
import com.gsc.tvcmanager.repository.toyota.IndicatorRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DateTimerTasks;
import com.sc.commons.utils.FileTasks;
import com.sc.commons.utils.StringTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.ArrayList;
import com.rg.objects.DealerCode;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j
public class IndicatorServiceImpl implements IndicatorService {

    private final DealerUtils dealerUtils;
    public static final String STATUS_OPEN		= "Aberto";
    public static final String STATUS_CLOSED	= "Fechado";
    public static final String STATUS_REOPEN	= "Reaberto";
    public static final String STATUS = "status";
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
    @Transactional
    public void saveIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, SaveIndicatorsDTO saveIndicatorsDTO) {

        int id = StringTasks.cleanInteger(saveIndicatorsDTO.getId(), 0);
        String oidNet = userPrincipal.getOidNet();
        String oidDealer = StringTasks.cleanString(saveIndicatorsDTO.getOidDealer(), userPrincipal.getOidDealerParent());
        String status = StringTasks.cleanString(saveIndicatorsDTO.getStatus(), STATUS_OPEN);
        String totUsedCars = saveIndicatorsDTO.getTotUsedCars();
        int year = StringTasks.cleanInteger(saveIndicatorsDTO.getYear(), DateTimerTasks.getCurYear());
        int month = StringTasks.cleanInteger(saveIndicatorsDTO.getMonth(), DateTimerTasks.getCurMonth());
        TVCUsedCarsIndicatorsSales oUsedCarsIndicatorsSales = null;
        String userStamp = userPrincipal.getUsername().split("\\|\\|")[0]+"||"+userPrincipal.getUsername().split("\\|\\|")[1];

        if (oidNet == null || oidNet.equals("") || oidDealer.equals("")) {
            log.warn("id.................:" + id);
            log.warn("oidNet.............:" + oidNet);
            log.warn("oidDealer...:" + oidDealer);
            throw new IndicatorClientException("Impossóvel guardar informaçõo. Existem parâmetros não definidos \n " +
                    "id.................:" +id+ "\n" +
                    "oidNet.............:" +oidNet+ "\n" +
                    "oidDealer...:" +oidDealer+ "\n" );
        }

        try {
            if (id == 0) {
                oUsedCarsIndicatorsSales = new TVCUsedCarsIndicatorsSales();
                oUsedCarsIndicatorsSales.setOidDealer(StringTasks.cleanString(oidDealer, userPrincipal.getOidDealerParent()));
                oUsedCarsIndicatorsSales.setMonth(month);
                oUsedCarsIndicatorsSales.setYear(year);
            } else {
                oUsedCarsIndicatorsSales = indicatorRepository.findById(id).orElseThrow(()-> new IndicatorsException("Id not found: " + id));
                oUsedCarsIndicatorsSales.setChangedBy(userStamp);
            }
            if (totUsedCars != null)
                oUsedCarsIndicatorsSales.setTotUsedCars(StringTasks.cleanInteger(totUsedCars, 0));
            oUsedCarsIndicatorsSales.setStatus(status);

            List<TVCUsedCarsIndicatorsSalesLines> vecUsedCarsIndicatorsSalesLines = new ArrayList<>();
            TVCUsedCarsIndicatorsSalesLines oUsedCarsIndicatorsSalesLines = null;

            for (SalesLinesDTO currentSalesLine: saveIndicatorsDTO.getSalesLinesList()) {
                double indicatorValue = StringTasks.cleanDouble(currentSalesLine.getIndicatorValue(), -1);
                String usedCarsIndicators = StringTasks.cleanString(currentSalesLine.getUsedCarsIndicators(), "-1");
                oUsedCarsIndicatorsSalesLines = new TVCUsedCarsIndicatorsSalesLines();
                oUsedCarsIndicatorsSalesLines.setUsedCarsIndicators(usedCarsIndicators);
                oUsedCarsIndicatorsSalesLines.setIndicatorValue(indicatorValue);
                oUsedCarsIndicatorsSalesLines.setChangedBy(userStamp);
                vecUsedCarsIndicatorsSalesLines.add(oUsedCarsIndicatorsSalesLines);
            }
            indicatorRepository.mergeUsedCarsIndicatorsSales(oUsedCarsIndicatorsSales, vecUsedCarsIndicatorsSalesLines, userStamp);
        } catch (Exception e) {
            throw new IndicatorsException("Erro ao guardar formulário do Plano de Indicadores da Rede (Venda Usados)");
        }
    }

    @Override
    public UsedCarsIndicatorDTO getIndicatorsUsedSalesInfo(UserPrincipal userPrincipal, String oidDealer, Integer year, Integer month) {
        try {
            TVCUsedCarsIndicatorsSales oUsedCarsIndicatorsSales;
            boolean notHasEuroLinea = false;

            Dealer oDealer;
            oDealer = dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer);
            if (oDealer != null) {
                List<DealerCode> vecCodes = dealerUtils.getDealerCodes(userPrincipal.getOidNet(), oDealer);
                if (vecCodes == null || vecCodes.isEmpty())
                    notHasEuroLinea = true;
            }
            boolean isValidOpenMonth = false;
            int actualYear = DateTimerTasks.getCurYear();
            int actualMonth = DateTimerTasks.getCurMonth();
            oUsedCarsIndicatorsSales = indicatorRepository.getUsedCarsIndicatorsSales(year, month, oidDealer).orElse(null);
            if (oUsedCarsIndicatorsSales!=null && oUsedCarsIndicatorsSales.getStatus().equals(STATUS_OPEN)) {
                if ((oUsedCarsIndicatorsSales.getYear() == actualYear && oUsedCarsIndicatorsSales.getMonth() == actualMonth - 1) || (oUsedCarsIndicatorsSales.getYear() == actualYear - 1 && oUsedCarsIndicatorsSales.getMonth() == actualMonth + 11)) {
                    isValidOpenMonth = true;
                }
            } else if (oUsedCarsIndicatorsSales == null) {
                if ((year == actualYear && month == actualMonth - 1) || (year == actualYear - 1 && month == actualMonth + 11)) {
                    oUsedCarsIndicatorsSales = TVCUsedCarsIndicatorsSales.builder().year(year).month(month).build();
                    isValidOpenMonth = true;
                }
            }
            if (oUsedCarsIndicatorsSales != null && STATUS_REOPEN.equals(oUsedCarsIndicatorsSales.getStatus())) {
                isValidOpenMonth = true;
            }
            return UsedCarsIndicatorDTO.builder()
                    .existInBD(oUsedCarsIndicatorsSales != null ? true : false)
                    .notHasEuroLinea(notHasEuroLinea)
                    .usedCarsIndicatorsSales(oUsedCarsIndicatorsSales)
//                    .dealers(vecDealers)
                    .isCa(false)
                    .filterBeanisValidOpenMonth(isValidOpenMonth)
                    .build();
        }catch (Exception e){
            log.error("Visualizar Plano de Indicadores de Venda Usados\",\"Erro ao visualizar formul�rio do Plano de Indicadores de Venda Usados");
            throw  new SalesException("Erro ao visualizar formul�rio do Plano de Indicadores de Venda Usados", e);
        }
    }

    @Override
    public List<Dealer>  getDealers(UserPrincipal userPrincipal, String oidDealer) {
        List<Dealer> vecDealers = new ArrayList<>();
        try {
            if (userPrincipal.getRoles().contains(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS))
                return dealerUtils.getActiveMainDealersForServices(userPrincipal.getOidNet());

            vecDealers.add(dealerUtils.getByObjectId(userPrincipal.getOidNet(), oidDealer));
            return vecDealers;
        } catch (Exception e) {
            throw  new SalesException("Error fetching dealers", e);
        }
    }


    @Override
    public PrevisionFilterBean setFilter(UserPrincipal userPrincipal) {
        return PrevisionFilterBean.builder()
               .oidNet(userPrincipal.getOidNet())
               .year(DateTimerTasks.getCurMonth() == 1 ? DateTimerTasks.getCurYear() - 1:DateTimerTasks.getCurYear())
               .month(DateTimerTasks.getCurMonth() == 1 ? 12:DateTimerTasks.getCurMonth())
               .oidDealer(userPrincipal.getOidDealer())
               .build();
    }
}
