package com.gsc.tvcmanager.controller;


import com.gsc.tvcmanager.constants.ApiEndpoints;
import com.gsc.tvcmanager.dto.IndicatorUsedFilesDTO;
import com.gsc.tvcmanager.security.UserPrincipal;
import com.gsc.tvcmanager.service.IndicatorService;
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
@Api(value = "", tags = "INDICATOR")
@RestController
@CrossOrigin("*")
public class IndicatorsController {

    private final IndicatorService indicatorService;


    @GetMapping(ApiEndpoints.INDICATORS_GET_FILES_LIST)
    public ResponseEntity<IndicatorUsedFilesDTO> getIndicatorsUsedFilesList(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                            @RequestParam String oidDealer, @RequestParam Integer year,
                                                                            @RequestParam String uploadDir ) {
        IndicatorUsedFilesDTO indicatorsUsedFilesList = indicatorService.getIndicatorsUsedFilesList(userPrincipal, oidDealer, year, uploadDir);
        return ResponseEntity.status(HttpStatus.OK).body(indicatorsUsedFilesList);
    }

    @PostMapping(ApiEndpoints.SAVE_USED_CARS_INDICATORS_SALES)
    public ResponseEntity<IndicatorUsedFilesDTO> saveIndicatorsUsedSalesInfo(@AuthenticationPrincipal UserPrincipal userPrincipal,
                                                                            @RequestParam String oidDealer, @RequestParam String year,
                                                                            @RequestParam String uploadDir ) {
        IndicatorUsedFilesDTO indicatorsUsedFilesList = indicatorService.saveIndicatorsUsedSalesInfo(userPrincipal, oidDealer, "","","","",year, uploadDir);
        return ResponseEntity.status(HttpStatus.OK).body(indicatorsUsedFilesList);
    }

}
