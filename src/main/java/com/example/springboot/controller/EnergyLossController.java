package com.example.springboot.controller;


import com.example.springboot.common.ServiceResponse;
import com.example.springboot.entity.Network;
import com.example.springboot.service.SimulateService;
import com.example.springboot.service.impl.SimulateServiceImpl;
import com.example.springboot.simulate.EnergyConsumption;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EnergyLossController {

    @Resource
    private SimulateService simulateService;

    @Resource
    private SimulateServiceImpl simulateServiceImpl;

    /**
     * 能耗计算，将 network中的  energyConsumption 属性  在  simulate  中赋值并进行计算
     */
    @GetMapping(value = "/energyLoss")
    public ServiceResponse energyLossCal() {
        Network network = simulateService.simulate();
        EnergyConsumption energyConsumption = network.getEnergyConsumption();
        return ServiceResponse.createBySuccess(energyConsumption);
    }

    @GetMapping(value = "/energyLossOptimize")
    public ServiceResponse energyLossOptimizeT() {

        Network network = simulateServiceImpl.optimizeEnergyLoss();
        return ServiceResponse.createBySuccess(network);
    }


    @GetMapping(value = "/energyLossMatrix")
    public ServiceResponse energyLossMatrix() {
        List<ArrayList<Double>> energyLossMatrix = simulateServiceImpl.energyLossMatrix();

        return ServiceResponse.createBySuccess(energyLossMatrix);
    }
}
