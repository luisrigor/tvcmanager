package com.gsc.tvcmanager.service;

import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SalesLinesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.exceptions.IndicatorClientException;
import com.gsc.tvcmanager.exceptions.IndicatorsException;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.repository.toyota.IndicatorRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.impl.IndicatorServiceImpl;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ActiveProfiles(SecurityData.ACTIVE_PROFILE)
public class IndicatorServiceTest {

    @Mock
    private DealerUtils dealerUtils;

    @Mock
    private IndicatorRepository indicatorRepository;

    @InjectMocks
    private IndicatorServiceImpl indicatorService;

    private SecurityData securityData;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenGetIndicatorsUsedFilesListThenReturnInfo() throws SCErrorException {
        UserPrincipal userPrincipal = securityData.getUserPrincipal();
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(dealerUtils.getByObjectId(anyString(), anyString())).thenReturn(TVCData.getDealers().get(0));

        IndicatorUsedFilesDTO filesList = indicatorService.getIndicatorsUsedFilesList(userPrincipal, "1", 1, "./");


        assertEquals("s",filesList.getVecDealers().get(0).getSalesCode());
        assertEquals("1",filesList.getVecDealers().get(0).getDealerCode());

    }

    @Test
    void whenGetIndicatorsUsedFilesListThenThrows() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getByObjectId(anyString(), anyString())).thenThrow(RuntimeException.class);

        assertThrows(IndicatorsException.class ,()->indicatorService.getIndicatorsUsedFilesList(userPrincipal, "1", 1, "/home"));
    }

    @Test
    void whenSaveIndicatorsUsedSalesInfoThenReturn() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        SalesLinesDTO salesLinesDTO = SalesLinesDTO.builder()
                .indicatorValue("1")
                .usedCarsIndicators("1")
                .build();

        List<SalesLinesDTO> list = new ArrayList<>();
        list.add(salesLinesDTO);

        SaveIndicatorsDTO saveIndicatorsDTO = SaveIndicatorsDTO.builder()
                .id("0")
                .oidDealer("1")
                .totUsedCars("t")
                .year("1")
                .month("1")
                .salesLinesList(list)
                .build();

        doNothing().when(indicatorRepository).mergeUsedCarsIndicatorsSales(any(), any(), anyString());

        indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, saveIndicatorsDTO);

        verify(indicatorRepository, times(0)).findById(any());
        verify(indicatorRepository, times(1)).mergeUsedCarsIndicatorsSales(any(), any(), anyString());
    }

    @Test
    void whenSaveIndicatorsUsedSalesWithIdInfoThenReturn() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        SalesLinesDTO salesLinesDTO = SalesLinesDTO.builder()
                .indicatorValue("1")
                .usedCarsIndicators("1")
                .build();

        List<SalesLinesDTO> list = new ArrayList<>();
        list.add(salesLinesDTO);

        SaveIndicatorsDTO saveIndicatorsDTO = SaveIndicatorsDTO.builder()
                .id("1")
                .oidDealer("1")
                .totUsedCars("t")
                .year("1")
                .month("1")
                .salesLinesList(list)
                .build();


        when(indicatorRepository.findById(any())).thenReturn(Optional.of(TVCData.getIndicatorSales()));
        doNothing().when(indicatorRepository).mergeUsedCarsIndicatorsSales(any(), any(), anyString());

        indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, saveIndicatorsDTO);

        verify(indicatorRepository, times(1)).findById(any());
        verify(indicatorRepository, times(1)).mergeUsedCarsIndicatorsSales(any(), any(), anyString());
    }

    @Test
    void whenSaveIndicatorsUsedSalesInfoWithNullThenThrows() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet(null);
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        SalesLinesDTO salesLinesDTO = SalesLinesDTO.builder()
                .indicatorValue("1")
                .usedCarsIndicators("1")
                .build();

        List<SalesLinesDTO> list = new ArrayList<>();
        list.add(salesLinesDTO);

        SaveIndicatorsDTO saveIndicatorsDTO = SaveIndicatorsDTO.builder()
                .id("1")
                .oidDealer("1")
                .totUsedCars("t")
                .year("1")
                .month("1")
                .salesLinesList(list)
                .build();


        assertThrows(IndicatorClientException.class,()->indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, saveIndicatorsDTO));
    }

    @Test
    void whenSaveIndicatorsUsedSalesInfoThenThrows() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        SalesLinesDTO salesLinesDTO = SalesLinesDTO.builder()
                .indicatorValue("1")
                .usedCarsIndicators("1")
                .build();

        List<SalesLinesDTO> list = new ArrayList<>();
        list.add(salesLinesDTO);

        SaveIndicatorsDTO saveIndicatorsDTO = SaveIndicatorsDTO.builder()
                .id("1")
                .oidDealer("1")
                .totUsedCars("t")
                .year("1")
                .month("1")
                .salesLinesList(list)
                .build();

        when(indicatorRepository.findById(any())).thenThrow(RuntimeException.class);

        assertThrows(IndicatorsException.class,()->indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, saveIndicatorsDTO));
    }

    @Test
    void whenGetIndicatorsUsedSalesInfoThenReturn() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_PRF_TOYOTA_LEXUS_TCAP);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");


        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(dealerUtils.getByObjectId(anyString(), anyString())).thenReturn(TVCData.getDealers().get(0));
        when(dealerUtils.getDealerCodes(anyString(), any())).thenReturn(TVCData.getDealerCodes());

        when(indicatorRepository.getUsedCarsIndicatorsSales(anyInt(), anyInt(), anyString())).thenReturn(Optional.empty());

        UsedCarsIndicatorDTO indicatorsUsedSalesInfo = indicatorService.getIndicatorsUsedSalesInfo(userPrincipal, "1", 2023, 6);

        assertTrue(indicatorsUsedSalesInfo.isExistInBD());
        assertFalse(indicatorsUsedSalesInfo.isCa());
    }

    @Test
    void whenGetIndicatorsUsedSalesInfoWithProfileThenReturn() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(dealerUtils.getByObjectId(anyString(), anyString())).thenReturn(TVCData.getDealers().get(0));
        when(dealerUtils.getDealerCodes(anyString(), any())).thenReturn(new ArrayList<>());

        when(indicatorRepository.getUsedCarsIndicatorsSales(anyInt(), anyInt(), anyString())).thenReturn(Optional.of(TVCData.getIndicatorSales()));

        UsedCarsIndicatorDTO indicatorsUsedSalesInfo = indicatorService.getIndicatorsUsedSalesInfo(userPrincipal, "1", 1, 1);

        assertTrue(indicatorsUsedSalesInfo.isExistInBD());
        assertFalse(indicatorsUsedSalesInfo.isCa());
        assertEquals(1, indicatorsUsedSalesInfo.getUsedCarsIndicatorsSales().getId());
        assertEquals(2023, indicatorsUsedSalesInfo.getUsedCarsIndicatorsSales().getYear());
        assertEquals(6, indicatorsUsedSalesInfo.getUsedCarsIndicatorsSales().getMonth());
        assertEquals("Aberto", indicatorsUsedSalesInfo.getUsedCarsIndicatorsSales().getStatus());
    }

    @Test
    void whenGetIndicatorsUsedSalesInfoWithProfileThenThrows() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getByObjectId(anyString(), anyString())).thenThrow(RuntimeException.class);
        when(dealerUtils.getDealerCodes(anyString(), any())).thenReturn(new ArrayList<>());

        when(indicatorRepository.getUsedCarsIndicatorsSales(anyInt(), anyInt(), anyString())).thenReturn(Optional.of(TVCData.getIndicatorSales()));

        assertThrows(SalesException.class ,()->indicatorService.getIndicatorsUsedSalesInfo(userPrincipal, "1", 1, 1));
    }

    @Test
    void whenGetFilterThenReturnInfo() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("1");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        PrevisionFilterBean previsionFilterBean = indicatorService.setFilter(userPrincipal);

        assertEquals(2023, previsionFilterBean.getYear());
        assertEquals(7, previsionFilterBean.getMonth());
        assertEquals("1", previsionFilterBean.getOidNet());
        assertEquals("1", previsionFilterBean.getOidDealer());
    }

}
