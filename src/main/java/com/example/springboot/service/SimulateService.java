package com.example.springboot.service;

import com.example.springboot.entity.Network;

public interface SimulateService {
    Network simulate();

    Network optimizeTemperature();

    Network optimizeQuantity();

    Network optimizeEnergyLoss();

}


