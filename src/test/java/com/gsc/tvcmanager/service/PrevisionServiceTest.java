package com.gsc.tvcmanager.service;


import com.gsc.tvcmanager.constants.AppProfile;
import com.gsc.tvcmanager.dto.SavePrevisionDTO;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.exceptions.SalesException;
import com.gsc.tvcmanager.repository.toyota.PrevisionRepository;
import com.gsc.tvcmanager.repository.toyota.TVCUsedCarsPrevisionSalesRepository;
import com.gsc.tvcmanager.sample.data.provider.SecurityData;
import com.gsc.tvcmanager.sample.data.provider.TVCData;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.impl.PrevisionServiceImpl;
import com.gsc.tvcmanager.utils.DealerUtils;
import com.sc.commons.exceptions.SCErrorException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ActiveProfiles(SecurityData.ACTIVE_PROFILE)

public class PrevisionServiceTest {

    @Mock
    private DealerUtils dealerUtils;
    @Mock
    private PrevisionRepository previsionRepository;
    @Mock
    private TVCUsedCarsPrevisionSalesRepository tvcUsedCarsPrevisionSalesRepository;

    @InjectMocks
    private PrevisionServiceImpl previsionService;

    private SecurityData securityData;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        securityData = new SecurityData();
    }

    @Test
    void whenGetUsedCarsAllPrevisionSalesMonthOrYearTheReturnInfo() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHt(any(), any(), anyString()))
                .thenReturn(TVCData.getCarsPrevision());

        when(previsionRepository.previsionMonthly(any(), any(), any()))
                .thenReturn(TVCData.getUsedCarsPrevision());

        UsedCarsPrevisionDTO usedCarsPrevision = previsionService.getUsedCarsAllPrevisionSalesMonthOrYear(userPrincipal, 1, 1, false);


        assertEquals("1",usedCarsPrevision.getVecDealers().get(0).getObjectId());
        assertEquals("1",usedCarsPrevision.getVecDealers().get(0).getResp());
        assertEquals("1", usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getOidDealer());
        assertEquals(1, usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getPrevisionSn());
        assertEquals(1, usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getPrevisionTvc());

    }

    @Test
    void whenGetUsedCarsAllPrevisionSalesMonthTheReturnInfo() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenReturn(TVCData.getDealers());
        when(tvcUsedCarsPrevisionSalesRepository.getAllPrevisionHt(any(), any(), anyString()))
                .thenReturn(TVCData.getCarsPrevision());

        when(previsionRepository.previsionMonthly(any(), any(), any()))
                .thenReturn(TVCData.getUsedCarsPrevision());

        UsedCarsPrevisionDTO usedCarsPrevision = previsionService.getUsedCarsAllPrevisionSalesMonthOrYear(userPrincipal, 1, 1, true);


        assertEquals("1",usedCarsPrevision.getVecDealers().get(0).getObjectId());
        assertEquals("1",usedCarsPrevision.getVecDealers().get(0).getResp());
        assertEquals("1", usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getPrevisionType());
        assertEquals(1, usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getPrevisionSn());
        assertEquals(1, usedCarsPrevision.getHstUsedCarsPrevisionSales().get(0).getPrevisionTvc());

    }

    @Test
    void whenGetUsedCarsAllPrevisionSalesMonthThenThrows() throws SCErrorException {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(dealerUtils.getActiveMainDealersForServices(anyString())).thenThrow(RuntimeException.class);

        assertThrows(RuntimeException.class, ()->previsionService.getUsedCarsAllPrevisionSalesMonthOrYear(userPrincipal, 1, 1, true));
    }

    @Test
    void whenSaveUsedCarsPrevisionSales() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(previsionRepository.findById(any())).thenReturn(Optional.of(TVCData.getUsedCarsPrevision().get(0)));
        SavePrevisionDTO usedCarsPrevision = SavePrevisionDTO.builder()
                .id(1)
                .oidDealer("1")
                .actualYear(1)
                .previsionSn(1)
                .previsionTvc(1)
                .status("s")
                .build();
        doNothing().when(previsionRepository).mergeUsedCarsPrevisionSales(any(), anyString());

        previsionService.saveUsedCarsPrevisionSales(userPrincipal, usedCarsPrevision);

        verify(previsionRepository, times(1)).findById(any());
        verify(previsionRepository).mergeUsedCarsPrevisionSales(any(), anyString());
    }

    @Test
    void whenSaveUsedCarsPrevisionSalesOtherId() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(previsionRepository.findById(any())).thenReturn(Optional.of(TVCData.getUsedCarsPrevision().get(0)));

        doNothing().when(previsionRepository).mergeUsedCarsPrevisionSales(any(), anyString());
        SavePrevisionDTO usedCarsPrevision = SavePrevisionDTO.builder()
                .id(1)
                .oidDealer("1")
                .actualYear(1)
                .previsionSn(1)
                .previsionTvc(1)
                .status("s")
                .build();
        previsionService.saveUsedCarsPrevisionSales(userPrincipal,usedCarsPrevision);

        verify(previsionRepository, times(1)).findById(any());
        verify(previsionRepository).mergeUsedCarsPrevisionSales(any(), anyString());
    }

    @Test
    void whenSaveUsedCarsPrevisionSalesOtherIdThenThrows() {
        Set<AppProfile> roles = new HashSet<>();
        roles.add(AppProfile.TVC_MANAGER_ROLE_ACTIVE_DEALERS);

        UserPrincipal userPrincipal =  new UserPrincipal("137||tcap1@tpo||tiago.fernandes@parceiro.rigorcg.pt",
                roles,33L);
        userPrincipal.setOidNet("SC00010001");
        userPrincipal.setOidDealerParent("1");
        userPrincipal.setOidDealer("1");

        when(previsionRepository.findById(any())).thenThrow(RuntimeException.class);
        SavePrevisionDTO usedCarsPrevision = SavePrevisionDTO.builder()
                .id(1)
                .oidDealer("1")
                .actualYear(1)
                .previsionSn(1)
                .previsionTvc(1)
                .status("s")
                .build();
        assertThrows(SalesException.class,()->previsionService.saveUsedCarsPrevisionSales(userPrincipal, usedCarsPrevision));

    }

}
