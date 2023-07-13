package com.gsc.tvcmanager.controller;


import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.SalesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
public class SalesController {

    private final SalesService salesService;

    @GetMapping(ApiEndpoints.USED_CARS_PREVISION_SALES)
    public ResponseEntity<UsedCarsPrevisionDTO> getUsedCarsPrevisionSales(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                          @RequestParam Integer year, @RequestParam Integer month, @RequestParam String oidDealer) {
        UsedCarsPrevisionDTO usedCarsPrevisionSales = salesService.getUsedCarsPrevisionSales(userPrincipal, oidDealer, year, month);
        return ResponseEntity.status(HttpStatus.OK).body(usedCarsPrevisionSales);
    }

    @GetMapping(ApiEndpoints.EXPORT_YEAR_REPORT)
    public ResponseEntity<String> getYearReport(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response,
                                                @RequestParam Integer year, @RequestParam Integer month, @RequestParam String oidDealer,
                                                @RequestParam boolean isOnlyYear) {
        salesService.getReportByYearAndMonth(userPrincipal, response, oidDealer, year, month, isOnlyYear);
        return ResponseEntity.status(HttpStatus.OK).body("generated");
    }

}
