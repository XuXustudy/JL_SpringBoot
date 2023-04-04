package com.example.springboot.controller;

import com.example.springboot.common.Result;
import com.example.springboot.entity.Node;
import com.example.springboot.entity.Pipe;
import com.example.springboot.service.impl.SimulateServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author xuyihao
 * @date 2023-02-06
 * @apiNote
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {

    @Resource
    private SimulateServiceImpl simulateService;

    @GetMapping("/simulate")
    public Result simulate() {
        List<Pipe> pipeList = simulateService.simulate().getPipeList();
        Map<String, Object> map = new HashMap<>();
        List<Double> QArr = new ArrayList<>();
        List<String> pipeNameArr = new ArrayList<>();
        for (int i = 0; i < pipeList.size(); i++) {
            Pipe pipe = pipeList.get(i);
            QArr.add(pipe.getQuantityM());
            pipeNameArr.add(pipe.getPipeName());
        }

        map.put("x", pipeNameArr);
        map.put("y", QArr);

        return Result.success(map);
    }

    @GetMapping("/simulatePressure")
    public Result simulatePressure() {
        List<Node> nodeList = simulateService.simulate().getNodeList();
        Map<String, Object> map = new HashMap<>();
        List<Double> preArr = new ArrayList<>();
        List<String> nodeNameArr = new ArrayList<>();
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            preArr.add(node.getPressure()/1E6);
            nodeNameArr.add(node.getNodeName());
        }

        map.put("x", nodeNameArr);
        map.put("y", preArr);

        return Result.success(map);
    }
}
