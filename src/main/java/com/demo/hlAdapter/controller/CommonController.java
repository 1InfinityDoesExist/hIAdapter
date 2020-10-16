package com.demo.hlAdapter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/adaptor")
@Slf4j
public class CommonController {


    @GetMapping(path = "/get/{tenantId}")
    public ResponseEntity<?> getControllerDetails(
                    @PathVariable(value = "tenantId", required = true) String tenantId) {
        log.info("::::::Inside CommonController Class, getContrllerDetails method::::");
        return ResponseEntity.status(HttpStatus.OK)
                        .body(new ModelMap().addAttribute("msg", "Success"));
    }
}
