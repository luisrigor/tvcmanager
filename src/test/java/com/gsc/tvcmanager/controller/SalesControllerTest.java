package com.gsc.tvcmanager.controller;

import com.google.gson.Gson;
import com.gsc.tvcmanager.config.SecurityConfig;
import com.gsc.tvcmanager.config.environment.EnvironmentConfig;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.repository.toyota.ClientRepository;
import com.gsc.tvcmanager.repository.toyota.ConfigurationRepository;
import com.gsc.tvcmanager.repository.toyota.LoginKeyRepository;
import com.gsc.tvcmanager.repository.toyota.ServiceLoginRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.TokenProvider;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.SalesService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(SalesController.class)
class SalesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private SalesService salesService;


    @MockBean
    private ConfigurationRepository configurationRepository;
    @MockBean
    private LoginKeyRepository loginKeyRepository;
    @MockBean
    private ServiceLoginRepository serviceLoginRepository;
    @MockBean
    private EnvironmentConfig environmentConfig;
    @MockBean
    private ClientRepository clientRepository;
    private Gson gson;
    private SecurityData securityData;

    private String BASE_REQUEST_MAPPING =  "/tvc-manager";
    private static String generatedToken;
    @BeforeEach
    void setUp() {
        gson = new Gson();
        securityData = new SecurityData();
        when(loginKeyRepository.findById(anyLong())).thenReturn(Optional.of(securityData.getLoginKey()));
    }

    @BeforeAll
    static void beforeAll() {
        SecurityData secData = new SecurityData();
        generatedToken = secData.generateNewToken();
    }


    @Test
    void whenSetFilterThenReturnInfo() throws Exception {
        PrevisionFilterBean previsionFilterBean = PrevisionFilterBean.builder()
                .oidDealer("1")
                .year(2023)
                .month(7)
                .actualMonth(7)
                .actualYear(2023)
                .build();

        String accessToken = generatedToken;

        when(salesService.setFilter(any(UserPrincipal.class))).thenReturn(previsionFilterBean);

        ResultActions resultActions = mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.PREVISION_SET_FILTER)
                .header("accessToken", accessToken)
                .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.oidDealer").value("1"))
                .andExpect(jsonPath("$.year").value(2023));
    }

    @Test
    void whenGetUsedCarsPrevisionSalesThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        UsedCarsPrevisionDTO usedCarsPrevisionDTO = UsedCarsPrevisionDTO.builder()
                .hstUsedCarsPrevisionSales(TVCData.getMonthPrevisionSales())
                .vecDealers(TVCData.getDealers())
                .build();

        when(salesService.getUsedCarsPrevisionSales(any(), anyString(), anyInt(), anyInt()))
                .thenReturn(usedCarsPrevisionDTO);

        mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.USED_CARS_PREVISION_SALES+"?year=1&month=1&oidDealer=1")
                        .header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetYearReportThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        doNothing().when(salesService).getReportByYearAndMonth(any(), any(), any(), anyInt(), anyInt(), anyBoolean());


        mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.EXPORT_YEAR_REPORT+"?year=1&month=1&oidDealer=1&isOnlyYear=false")
                        .header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void whenGetYearReportThenReturnUpdate() throws Exception {

        String accessToken = generatedToken;

        List<Integer> arrDealers = new ArrayList<>();
        arrDealers.add(1);
        arrDealers.add(2);

        doNothing().when(salesService).openMonthPrevision(any(), any());

        mvc.perform(put(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.OPEN_MONTH_PREVISION)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(arrDealers))
                )

                .andExpect(status().is2xxSuccessful());
    }


}
