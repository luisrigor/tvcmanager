package com.gsc.tvcmanager.service;


import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsPrevisionSalesRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.impl.SalesServiceImpl;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.sc.commons.exceptions.SCErrorException;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)

class SalesServiceTest {

    @Mock
    private TVCUsedCarsPrevisionSalesRepository tvcUsedCarsPrevisionSalesRepository;
    @Mock
    private DealerUtils dealerUtils;

    @InjectMocks
    private SalesServiceImpl salesService;

    private SecurityData securityData;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }


    @Test
    void whenSetFilterThenReturnInfo() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        PrevisionFilterBean filterBean = salesService.setFilter(userPrincipal);

        assertEquals("SC00010001", filterBean.getOidNet());
        assertEquals("1", filterBean.getOidDealer());
        assertEquals(8, filterBean.getMonth());
        assertEquals(2023, filterBean.getYear());
    }

    @Test
    void whenGetUsedCarsPrevisionSalesThenReturnInfo() throws SCErrorException {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(tvcUsedCarsPrevisionSalesRepository.getMonthPrevisionByDealerHt(anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(TVCData.getMonthPrevisionSales());

        UsedCarsPrevisionDTO usedCarsPrevisionSales = salesService.getUsedCarsPrevisionSales(userPrincipal, "1", 1, 1);

        assertEquals("1", usedCarsPrevisionSales.getVecDealers().get(0).getDealerCode());
        assertEquals("001", usedCarsPrevisionSales.getVecDealers().get(0).getCp3());
        assertEquals("a", usedCarsPrevisionSales.getVecDealers().get(0).getEmail());
        assertEquals("d", usedCarsPrevisionSales.getVecDealers().get(0).getDistrict());

        assertEquals(1, usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(0).getPrevisionSn());
        assertEquals(1, usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(0).getPrevisionTvc());
        assertEquals(1,usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(0).getId());
        assertEquals(1,usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(0).getMonth());
    }

    @Test
    void whenGetUsedCarsPrevisionSalesThenReturnInfoForOtherProfiles() throws SCErrorException {

        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);
        UserPrincipal userPrincipal = new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getByObjectId(anyString(), anyString())).thenReturn(TVCData.getDealers().get(0));
        when(tvcUsedCarsPrevisionSalesRepository.getMonthPrevisionByDealerHt(anyString(), anyInt(), anyInt(), anyString()))
                .thenReturn(TVCData.getMonthPrevisionSales());

        UsedCarsPrevisionDTO usedCarsPrevisionSales = salesService.getUsedCarsPrevisionSales(userPrincipal, "1", 1, 1);

        assertEquals("1", usedCarsPrevisionSales.getVecDealers().get(0).getDealerCode());
        assertEquals("001", usedCarsPrevisionSales.getVecDealers().get(0).getCp3());
        assertEquals("a", usedCarsPrevisionSales.getVecDealers().get(0).getEmail());
        assertEquals("d", usedCarsPrevisionSales.getVecDealers().get(0).getDistrict());

        assertEquals(-1, usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(1).getPrevisionSn());
        assertEquals(-1, usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(1).getPrevisionTvc());
        assertEquals("Mensal",usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(1).getPrevisionType());
        assertEquals("Aberto",usedCarsPrevisionSales.getHstUsedCarsPrevisionSales().get(1).getStatus());
    }

    @Test
    void whenGetUsedCarsPrevisionSalesThenThrows() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(tvcUsedCarsPrevisionSalesRepository.getMonthPrevisionByDealerHt(anyString(), anyInt(), anyInt(), anyString()))
                .thenThrow(SalesException.class);

        assertThrows(SalesException.class, ()-> salesService.getUsedCarsPrevisionSales(userPrincipal, "1", 1, 1));
    }

    @Test
    void whenGetReportByYearThenReturnInfo() throws IOException, SCErrorException {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        HttpServletResponse response = new MockHttpServletResponse();

        when(tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHt(anyInt(), any(), anyString()))
                .thenReturn(TVCData.getAllPrevisionSales());

        when(dealerUtils.getActiveMainDealersForServices(anyString()))
                .thenReturn(TVCData.getDealers());



        salesService.getReportByYearAndMonth(userPrincipal, response, "1", 1,1, true);

        InputStream targetStream = new ByteArrayInputStream(((MockHttpServletResponse) response).getContentAsByteArray());
        HSSFWorkbook workbook = new HSSFWorkbook(targetStream);
        HSSFSheet spreadsheet = workbook.getSheetAt(0);
        HSSFRow row;
        row = spreadsheet.getRow(1);
        row.getCell(1);


        assertEquals("OBJECTIVO TUC", spreadsheet.getRow(2).getCell(2).toString());
        assertEquals("TUC", spreadsheet.getRow(2).getCell(3).toString());
        assertEquals("COMPRAS TCAP", spreadsheet.getRow(2).getCell(4).toString());
        assertEquals("TUC/OBJETIVO", spreadsheet.getRow(2).getCell(5).toString());

        assertEquals("20.0", spreadsheet.getRow(3).getCell(2).toString());
        assertEquals("20.0", spreadsheet.getRow(3).getCell(3).toString());
        assertEquals("10.0", spreadsheet.getRow(3).getCell(4).toString());
    }

    @Test
    void whenGetReportByMonthThenReturnInfo() throws IOException, SCErrorException {

        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        HttpServletResponse response = new MockHttpServletResponse();

        when(tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHtD(anyInt(), any(), anyString(),anyString()))
                .thenReturn(TVCData.getAllPrevisionSalesHT());

        when(dealerUtils.getActiveMainDealersForServices(anyString()))
                .thenReturn(TVCData.getDealers());



        salesService.getReportByYearAndMonth(userPrincipal, response, "1", 1,1, false);

        InputStream targetStream = new ByteArrayInputStream(((MockHttpServletResponse) response).getContentAsByteArray());
        HSSFWorkbook workbook = new HSSFWorkbook(targetStream);
        HSSFSheet spreadsheet = workbook.getSheetAt(0);
        HSSFRow row;
        row = spreadsheet.getRow(1);
        row.getCell(1);


        assertEquals("OBJECTIVO TUC", spreadsheet.getRow(2).getCell(2).toString());
        assertEquals("TUC", spreadsheet.getRow(2).getCell(3).toString());
        assertEquals("COMPRAS TCAP", spreadsheet.getRow(2).getCell(4).toString());
        assertEquals("TUC/OBJETIVO", spreadsheet.getRow(2).getCell(5).toString());

        assertEquals("", spreadsheet.getRow(3).getCell(2).toString());
        assertEquals("", spreadsheet.getRow(3).getCell(3).toString());
        assertEquals("", spreadsheet.getRow(3).getCell(4).toString());
    }

    @Test
    void whenGetReportByMonthThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        HttpServletResponse response = new MockHttpServletResponse();

        when(tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHt(anyInt(), any(), anyString()))
                .thenThrow(RuntimeException.class);


        assertThrows(SalesException.class ,()->salesService.getReportByYearAndMonth(userPrincipal, response, "1", 1,1, true));

    }
    @Test
    void whenOpenMonthPrevisionThenUpdate() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        List<Integer> dealersToClose = new ArrayList<>();
        dealersToClose.add(1);
        dealersToClose.add(2);
        dealersToClose.add(3);

        doNothing().when(tvcUsedCarsPrevisionSalesRepository).changeUsedCarsPrevisionSalesStatus(anyInt(), anyString());

        salesService.openMonthPrevision(userPrincipal, dealersToClose);

        verify(tvcUsedCarsPrevisionSalesRepository, times(3)).changeUsedCarsPrevisionSalesStatus(anyInt(), anyString());
    }

    @Test
    void whenOpenMonthPrevisionThenThrows() {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        List<Integer> dealersToClose = new ArrayList<>();
        dealersToClose.add(1);
        dealersToClose.add(2);
        dealersToClose.add(3);

        doThrow(new RuntimeException("Error update")).when(tvcUsedCarsPrevisionSalesRepository).changeUsedCarsPrevisionSalesStatus(anyInt(), anyString());

        assertThrows(SalesException.class, ()-> salesService.openMonthPrevision(userPrincipal, dealersToClose));
    }
}
