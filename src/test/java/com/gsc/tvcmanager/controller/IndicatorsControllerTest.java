package com.gsc.tvcmanager.controller;


import com.google.gson.Gson;
import com.gsc.tvcmanager.config.SecurityConfig;
import com.gsc.tvcmanager.config.environment.EnvironmentConfig;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.repository.toyota.ClientRepository;
import com.gsc.tvcmanager.repository.toyota.ConfigurationRepository;
import com.gsc.tvcmanager.repository.toyota.LoginKeyRepository;
import com.gsc.tvcmanager.repository.toyota.ServiceLoginRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.TokenProvider;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
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

import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(IndicatorsController.class)
class IndicatorsControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private IndicatorService indicatorService;


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

        when(indicatorService.setFilter(any(UserPrincipal.class))).thenReturn(previsionFilterBean);

        ResultActions resultActions = mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.INDICATOR_SET_FILTER)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON))


                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.oidDealer").value("1"))
                .andExpect(jsonPath("$.year").value(2023));
    }

    @Test
    void whenGetIndicatorsUsedFilesLisThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        IndicatorUsedFilesDTO indicatorUsedFilesDTO = IndicatorUsedFilesDTO.builder()
                .vecDealers(TVCData.getDealers())
                .build();

        when(indicatorService.getIndicatorsUsedFilesList(any(), any(), anyInt(), anyString()))
                .thenReturn(indicatorUsedFilesDTO);

        ResultActions resultActions = mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.INDICATORS_GET_FILES_LIST+"?oidDealer=1&year=1&uploadDir=/home")
                        .header("accessToken", accessToken))


                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.vecDealers[0].ivDealerCode").value("1"))
                .andExpect(jsonPath("$.vecDealers[0].ivSalesCode").value("s"));
    }

    @Test
    void whenGetIndicatorsUsedSalesInfoThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        UsedCarsIndicatorDTO usedCarsIndicator = UsedCarsIndicatorDTO.builder()
                .existInBD(true)
                .notHasEuroLinea(false)
                .usedCarsIndicatorsSales(TVCData.getIndicatorSales())
                .build();

        when(indicatorService.getIndicatorsUsedSalesInfo(any(), any(), anyInt(), anyInt()))
                .thenReturn(usedCarsIndicator);

        ResultActions resultActions = mvc.perform(get(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.GET_USED_CARS_INDICATORS_SALES+"?oidDealer=1&year=1&month=1")
                        .header("accessToken", accessToken))


                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.existInBD").value(true))
                .andExpect(jsonPath("$.notHasEuroLinea").value(false))
                .andExpect(jsonPath("$.usedCarsIndicatorsSales.year").value(2023))
                .andExpect(jsonPath("$.usedCarsIndicatorsSales.status").value("Aberto"));
    }

    @Test
    void whenSaveIndicatorsUsedSalesInfoThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        SaveIndicatorsDTO usedCarsIndicator = SaveIndicatorsDTO.builder()
                .id("1")
                .oidDealer("SC003")
                .year("2023")
                .month("1")
                .status("S")
                .build();

        doNothing().when(indicatorService).saveIndicatorsUsedSalesInfo(any(), any());

        ResultActions resultActions = mvc.perform(post(BASE_REQUEST_MAPPING + "/" + ApiEndpoints.SAVE_USED_CARS_INDICATORS_SALES)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(usedCarsIndicator)))

                .andExpect(status().is2xxSuccessful());

    }
}
