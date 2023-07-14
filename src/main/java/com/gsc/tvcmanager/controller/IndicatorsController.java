package com.gsc.tvcmanager.controller;


import com.google.gson.Gson;
import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.dto.SaveIndicatorsDTO;
import com.gsc.tvcmanager.dto.UsedCarsIndicatorDTO;
import com.gsc.tvcmanager.model.toyota.entity.PrevisionFilterBean;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class IndicatorsController {

    private final IndicatorService indicatorService;

    @GetMapping(ApiEndpoints.INDICATOR_SET_FILTER)
    public ResponseEntity<PrevisionFilterBean> setFilter(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        PrevisionFilterBean previsionFilterBean = indicatorService.setFilter(userPrincipal);
        return ResponseEntity.status(HttpStatus.OK).body(previsionFilterBean);
    }

    @GetMapping(ApiEndpoints.INDICATORS_GET_FILES_LIST)
    public ResponseEntity<IndicatorUsedFilesDTO> getIndicatorsUsedFilesList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                            @RequestParam String oidDealer, @RequestParam Integer year,
                                                                            @RequestParam String uploadDir ) {
        IndicatorUsedFilesDTO indicatorsUsedFilesList = indicatorService.getIndicatorsUsedFilesList(userPrincipal, oidDealer, year, uploadDir);
        return ResponseEntity.status(HttpStatus.OK).body(indicatorsUsedFilesList);
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

}
