package com.gsc.tvcmanager.controller;

import com.google.gson.Gson;
import com.gsc.tvcmanager.config.SecurityConfig;
import com.gsc.tvcmanager.config.environment.EnvironmentConfig;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.SavePrevisionDTO;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.repository.toyota.ClientRepository;
import com.gsc.tvcmanager.repository.toyota.ConfigurationRepository;
import com.gsc.tvcmanager.repository.toyota.LoginKeyRepository;
import com.gsc.tvcmanager.repository.toyota.ServiceLoginRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.TokenProvider;
import com.gsc.tvcmanager.service.PrevisionService;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import({SecurityConfig.class, TokenProvider.class})
@ActiveProfiles(profiles = SecurityData.ACTIVE_PROFILE)
@WebMvcTest(PrevisionController.class)
class PrevisionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private PrevisionService previsionService;


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

    private String BASE_REQUEST_MAPPING = "/tvc-manager";
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
    void whenGetUsedCarsAllPrevisionSalesMonthThenReturnInfo() throws Exception {

        String accessToken = generatedToken;

        UsedCarsPrevisionDTO usedCarsPrevisionDTO = UsedCarsPrevisionDTO.builder()
                .hstUsedCarsPrevisionSales(TVCData.getMonthPrevisionSales())
                .vecDealers(TVCData.getDealers())
                .build();

        when(previsionService.getUsedCarsAllPrevisionSalesMonthOrYear(any(), any(), anyInt(), anyBoolean()))
                .thenReturn(usedCarsPrevisionDTO);

        mvc.perform(get(BASE_REQUEST_MAPPING +"/"+ ApiEndpoints.PREVISION_MONTH+"?month=1&year=2&isMontn=isMonth=true")
                        .header("accessToken", accessToken))

                .andExpect(status().is2xxSuccessful());


    }

    @Test
    void whenSaveUsedCarsPrevisionSalesThenReturnOk() throws Exception {

        String accessToken = generatedToken;

        SavePrevisionDTO usedCarsPrevision = SavePrevisionDTO.builder()
                .id(660)
                .oidDealer("SC00020073")
                .actualYear(2023)
                .previsionSn(302)
                .previsionTvc(301)
                .status("Aberto")
                .build();
        doNothing().when(previsionService).saveUsedCarsPrevisionSales(any(), any());
        mvc.perform(post(BASE_REQUEST_MAPPING +"/"+ ApiEndpoints.SAVE_USED_CARS_PREVISION_SALES)
                        .header("accessToken", accessToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(usedCarsPrevision)))
                .andExpect(status().is2xxSuccessful());

    }

}
