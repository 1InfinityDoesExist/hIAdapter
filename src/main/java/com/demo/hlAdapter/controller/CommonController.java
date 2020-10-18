package com.demo.hlAdapter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.amazonaws.services.codecommit.model.UserInfo;
import com.demo.hlAdapter.config.producer.HlaProducer;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/adaptor")
@Slf4j
public class CommonController {

    @Autowired
    private HlaProducer hlaProducer;

    @GetMapping(path = "/get/{tenantId}")
    public ResponseEntity<?> getControllerDetails(
                    @PathVariable(value = "tenantId", required = true) String tenantId) {
        log.info("::::::Inside CommonController Class, getContrllerDetails method::::");
        return ResponseEntity.status(HttpStatus.OK)
                        .body(new ModelMap().addAttribute("msg", "Success"));
    }

    @PostMapping(value = "/persist/userInfo/{topic}")
    public ResponseEntity<?> saveUserRole(
                    @PathVariable(value = "topic", required = true) String topic,
                    @RequestBody String userRole) {
        log.info(":::::CommonController Class, saveUserRole method::::");
        hlaProducer.sendMessage(topic, userRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ModelMap().addAttribute("msg",
                        "Successfully persisted userInfo into db."));
    }
}
