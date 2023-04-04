package com.example.springboot.controller;


import com.example.springboot.common.ServiceResponse;
import com.example.springboot.entity.Network;
import com.example.springboot.service.SimulateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
public class SimulateController {

    @Resource
    private SimulateService simulateService;

    @GetMapping(value = "/simulate")
    public ServiceResponse simulate() {
        Network network = simulateService.simulate();
        return ServiceResponse.createBySuccess(network);
    }

    @GetMapping(value = "/simulate/optimizeTemperature")
    public ServiceResponse optimizeTempertise() {
        Network network = simulateService.optimizeTemperature();
        return ServiceResponse.createBySuccess(network);
    }

    @GetMapping(value = "/simulate/optimizeQuantityM")
    public ServiceResponse optimizeQuantityM() {
        Network network = simulateService.optimizeQuantity();
        return ServiceResponse.createBySuccess(network);
    }

}
