package com.gsc.tvcmanager.controller;

import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("${app.baseUrl}")
@Api(value = "",tags = "TVC MANAGER")
@RestController
@CrossOrigin("*")
public class TvcManagerController {

        @GetMapping(value="/")
        public String test(){
            return "TVCManager!!";
        }
}
