package com.gsc.tvcmanager.service.impl;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.PrevisionHtDTO;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.TVCUsedCarsPrevisionSales;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsPrevisionSalesRepository;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.SalesService;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.gsc.tvcmanager.utils.ReportUtils;
import com.rg.dealer.Dealer;
import com.sc.commons.exceptions.SCErrorException;
import com.sc.commons.utils.DateTimerTasks;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.gsc.tvcmanager.utils.ReportUtils.*;

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
    private final static String SHEET_YEAR_NAME 					= "Acumulado Anual";


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
    public void getYearReport(UserPrincipal userPrincipal, HttpServletResponse response, String oidDealer, Integer year, Integer month) {

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String filename = "Previsao_vendas_TUC_" + year +".xls";
        String headerValue = "attachment; filename="+filename;

        response.setHeader(headerKey, headerValue);

        try {
            boolean isOnlyYear = false;

            HSSFWorkbook workBook = new HSSFWorkbook();
            HSSFSheet monthSheet = null;
            if(!isOnlyYear)
                monthSheet = workBook.createSheet(DateTimerTasks.ptMonths[month-1]);
            HSSFSheet yearSheet = workBook.createSheet(SHEET_YEAR_NAME);

            int currentRow = 1;
            if(!isOnlyYear){
                currentRow = createTitleLines(workBook, monthSheet, month, year, currentRow, userPrincipal.getOidNet());
                createLinesValues(workBook, monthSheet, month, year, currentRow, userPrincipal.getOidNet());
            }
            currentRow = 1;
            currentRow = createTitleLines(workBook, yearSheet, month, year, currentRow, userPrincipal.getOidNet());
            createLinesValues(workBook, yearSheet, month, year, currentRow, userPrincipal.getOidNet());

            ServletOutputStream outputStream = response.getOutputStream();
            workBook.write(outputStream);
            workBook.close();
            outputStream.close();

        }catch (Exception e) {
            throw new SalesException("Error generating report", e);
        }

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

    private void createLinesValues(HSSFWorkbook workBook, HSSFSheet sheet, int month, int year, int currentRow, String oidNet) throws SCErrorException {
        HSSFRow row = null;

        HSSFCellStyle styleDetailsCenterText = ReportUtils.getDetailStyle(workBook, HorizontalAlignment.CENTER, "NUMBER");

        List<Dealer> vecDealers = new ArrayList<>();
        List<TVCUsedCarsPrevisionSales> hstPrevisionTcap = new ArrayList<>();
        List<TVCUsedCarsPrevisionSales> hstPrevisionConc = new ArrayList<>();

        String sheetType = "EXCELL";
        int initialRow = currentRow;
        int column = 1;
        int tcapPrevision = 0;
        int concPrevisionTvc = 0;
        int concPrevisionSn = 0;
        if (sheet.getSheetName().equals(SHEET_YEAR_NAME)) {
            sheetType = "ANUAL";
        }

        vecDealers = dealerUtils.getActiveMainDealersForServices(oidNet);

        hstPrevisionConc = tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHtD(year, month, sheetType, PREVISION_TYPE_MENSAL);
        hstPrevisionTcap = tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHtD(year, month, sheetType, PREVISION_TYPE_ANUAL);

        List<TVCUsedCarsPrevisionSales>  oUsedCarsPrevisionSalesAux = null;


        for(Dealer oDealer: vecDealers) {
            String strDealer = oDealer!=null?oDealer.getDesig():"";
           oUsedCarsPrevisionSalesAux = hstPrevisionTcap.stream()
                    .filter(tvcUsedCarsPrevisionSales -> tvcUsedCarsPrevisionSales.getOidDealer().equals(oDealer.getObjectId()))
                    .collect(Collectors.toList());

            tcapPrevision = !oUsedCarsPrevisionSalesAux.isEmpty()? oUsedCarsPrevisionSalesAux.get(0).getPrevisionTvc():0;
            oUsedCarsPrevisionSalesAux = hstPrevisionConc.stream()
                    .filter(tvcUsedCarsPrevisionSales -> tvcUsedCarsPrevisionSales.getOidDealer().equals(oDealer.getObjectId()))
                    .collect(Collectors.toList());
            concPrevisionTvc = !oUsedCarsPrevisionSalesAux.isEmpty()? oUsedCarsPrevisionSalesAux.get(0).getPrevisionTvc():0;
            concPrevisionSn = !oUsedCarsPrevisionSalesAux.isEmpty()? oUsedCarsPrevisionSalesAux.get(0).getPrevisionSn():0;
            currentRow++;
            column=1;
            row = sheet.createRow(currentRow);
            createCell(row, column, strDealer, getDetailStyle(workBook, HorizontalAlignment.LEFT, "TEXT"));
            column++;

            if(tcapPrevision > 0 ){
                createCellNumeric(row, column, tcapPrevision, styleDetailsCenterText);
            }else{
                createCell(row, column, "", styleDetailsCenterText);
            }
            column++;
            if(concPrevisionTvc > 0){
                createCellNumeric(row, column, concPrevisionTvc, styleDetailsCenterText);
            }else{
                createCell(row, column, "", styleDetailsCenterText);
            }
            column++;
            if(concPrevisionSn > 0){
                createCellNumeric(row, column, concPrevisionSn, styleDetailsCenterText);
            }else{
                createCell(row, column, "", styleDetailsCenterText);
            }
            createCellFormula(row, ++column, "TVC", getDetailStyle(workBook, HorizontalAlignment.CENTER, "PERCENT"));
            createCellFormula(row, ++column, "SN", getDetailStyle(workBook, HorizontalAlignment.CENTER, "PERCENT"));
        }

        if(currentRow>initialRow){
            HSSFCellStyle styleTitle = getTitleStyle(workBook, "TEXT", oidNet);
            currentRow++;
            column=1;
            row = sheet.createRow(currentRow);
            createCell(row, column,"TOTAL", styleTitle);
            createCellTtFormula(row, ++column, "OBJETIVO", styleTitle);
            createCellTtFormula(row, ++column, "TVC", styleTitle);
            createCellTtFormula(row, ++column, "SN", styleTitle);
            createCellFormula(row, ++column, "TVC", getTitleStyle(workBook, "PERCENT", oidNet));
            createCellFormula(row, ++column, "SN", getTitleStyle(workBook, "PERCENT", oidNet));
        }
    }
}
