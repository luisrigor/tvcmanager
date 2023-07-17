package com.gsc.tvcmanager.controller;


import com.google.gson.Gson;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.UsedCarsPrevisionDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.SalesService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}")
@Api(value = "", tags = "SALES")
@RestController
@CrossOrigin("*")

public class SalesController {

    private final SalesService salesService;

    @GetMapping(ApiEndpoints.PREVISION_SET_FILTER)
    public ResponseEntity<PrevisionFilterBean> setFilter(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        PrevisionFilterBean previsionFilterBean = salesService.setFilter(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(previsionFilterBean);
    }

    @GetMapping(ApiEndpoints.USED_CARS_PREVISION_SALES)
    public ResponseEntity<?> getUsedCarsPrevisionSales(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                          @RequestParam Integer year, @RequestParam Integer month, @RequestParam String oidDealer) {
        UsedCarsPrevisionDTO usedCarsPrevisionSales = salesService.getUsedCarsPrevisionSales(userPrincipal, oidDealer, year, month);
        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(usedCarsPrevisionSales));
    }

    @GetMapping(ApiEndpoints.EXPORT_YEAR_REPORT)
    public ResponseEntity<String> getYearReport(@AuthenticationPrincipal UserPrincipal userPrincipal, HttpServletResponse response,
                                                @RequestParam Integer year, @RequestParam Integer month, @RequestParam String oidDealer,
                                                @RequestParam boolean isOnlyYear) {
        salesService.getReportByYearAndMonth(userPrincipal, response, oidDealer, year, month, isOnlyYear);
        return ResponseEntity.status(HttpStatus.OK).body("generated");
    }

    @PutMapping(ApiEndpoints.OPEN_MONTH_PREVISION)
    public ResponseEntity<String> openMonthPrevision(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                     @RequestBody List<Integer> arrDealersToCloser) {
        salesService.openMonthPrevision(userPrincipal, arrDealersToCloser);
        return ResponseEntity.status(HttpStatus.OK).body("updated");
    }

}
