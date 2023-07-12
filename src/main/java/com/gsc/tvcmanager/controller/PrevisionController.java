package com.gsc.tvcmanager.controller;

import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.PrevisionService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}")
@Api(value = "", tags = "PREVISION")
@RestController
@CrossOrigin("*")
public class PrevisionController {

    private final PrevisionService previsionService;

    @GetMapping(ApiEndpoints.PREVISION_MONTH)
    public ResponseEntity<UsedCarsPrevisionDTO> getUsedCarsAllPrevisionSalesMonth(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        log.info("Client id " + userPrincipal.getClientId());
        UsedCarsPrevisionDTO previsionInfo= previsionService.getUsedCarsAllPrevisionSalesMonth(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(previsionInfo);
    }

    @PostMapping(ApiEndpoints.SAVE_USED_CARS_PREVISION_SALES)
    public void saveUsedCarsPrevisionSales(@AuthenticationPrincipal UserPrincipal userPrincipal,int id) {
        log.info("Client id " + userPrincipal.getClientId());
        previsionService.saveUsedCarsPrevisionSales(userPrincipal,id);
    }
}
