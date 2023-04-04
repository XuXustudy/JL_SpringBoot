package com.example.springboot;

import com.example.springboot.entity.Network;
import com.example.springboot.entity.Node;
import com.example.springboot.service.impl.SimulateServiceImpl;
import com.example.springboot.simulate.Simulate;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SimulateTest {
    @Resource
    SimulateServiceImpl simulateServiceImpl;
    @Test
    public void test1() {
        Network network= simulateServiceImpl.simulate();
    }

    @Test
    public void test2() {
        Network network = simulateServiceImpl.optimizeTemperature();
    }

    @Test
    public void test3() {
        Network network = simulateServiceImpl.optimizeQuantity();
    }
}
