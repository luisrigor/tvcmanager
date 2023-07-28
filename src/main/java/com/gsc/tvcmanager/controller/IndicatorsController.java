package com.gsc.tvcmanager.controller;


import com.google.gson.Gson;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
import com.rg.dealer.Dealer;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Log4j
@RequestMapping("${app.baseUrl}")
@Api(value = "", tags = "INDICATOR")
@RestController
@CrossOrigin("*")
public class IndicatorsController {

    private final IndicatorService indicatorService;

    @GetMapping(ApiEndpoints.INDICATOR_SET_FILTER)
    public ResponseEntity<PrevisionFilterBean> setFilter(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        PrevisionFilterBean previsionFilterBean = indicatorService.setFilter(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(previsionFilterBean);
    }

    @GetMapping(ApiEndpoints.INDICATORS_GET_FILES_LIST)
    public ResponseEntity<?> getIndicatorsUsedFilesList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                            @RequestParam String oidDealer, @RequestParam Integer year,
                                                                            @RequestParam String uploadDir ) {
        Gson gson = new Gson();
        IndicatorUsedFilesDTO indicatorsUsedFilesList = indicatorService.getIndicatorsUsedFilesList(userPrincipal, oidDealer, year, uploadDir);
        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(indicatorsUsedFilesList));
    }

    @PostMapping(ApiEndpoints.SAVE_USED_CARS_INDICATORS_SALES)
    public ResponseEntity<String> saveIndicatorsUsedSalesInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                             @RequestBody SaveIndicatorsDTO indicatorsDTO) {
        indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, indicatorsDTO);
        return ResponseEntity.status(HttpStatus.OK).body("saved");
    }

    @GetMapping(ApiEndpoints.GET_USED_CARS_INDICATORS_SALES)
    public ResponseEntity<?> getIndicatorsUsedSalesInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                           @RequestParam String oidDealer, @RequestParam Integer year,
                                                                           @RequestParam Integer month) {
        UsedCarsIndicatorDTO usedSales = indicatorService.getIndicatorsUsedSalesInfo(userPrincipal, oidDealer, year, month);
        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(usedSales));
    }

    @GetMapping(ApiEndpoints.GET_DEALERS)
    public ResponseEntity<?> getDealers(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                        @RequestParam String oidDealer) {
        List<Dealer> dealers = indicatorService.getDealers(userPrincipal, oidDealer);
        Gson gson = new Gson();

        return ResponseEntity.status(HttpStatus.OK).body(gson.toJson(dealers));
    }

}
