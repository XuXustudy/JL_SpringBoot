package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.*;
import com.example.springboot.mapper.NodeMapper;
import com.example.springboot.mapper.OilMapper;
import com.example.springboot.mapper.PipeMapper;
import com.example.springboot.service.SimulateService;
import com.example.springboot.simulate.EnergyConsumption;
import com.example.springboot.simulate.Simulate;
import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;

import java.util.Collections;
import java.util.List;

@Data
@Service
public class SimulateServiceImpl implements SimulateService {

    @Resource
    NodeMapper nodeMapper;

    @Resource
    PipeMapper pipeMapper;

    @Resource
    OilMapper oilMapper;

    private static final Logger logger = LoggerFactory.getLogger(SimulateServiceImpl.class);

    private Network network;


    public SimulateServiceImpl() {
    }

    /**
     * 查询数据库管网数据，将结果返回
     */
    public void init() {
        QueryWrapper<Node> Wrapper1 = new QueryWrapper<>();
        Wrapper1.eq("project_id", "1");

        QueryWrapper<Pipe> Wrapper2 = new QueryWrapper<>();
        Wrapper2.eq("project_id", "1");

        List<Node> nodes = nodeMapper.selectList(Wrapper1);
        List<Pipe> pipes = pipeMapper.selectList(Wrapper2);


        Network network = new Network(nodes, pipes);

        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            int loadFluidId = node.getLoadFluidId();
            if (loadFluidId == 0) {   //为纯水
                Fluid2 fluid2 = new Fluid2();
                node.setLoadFluid(fluid2);

            } else if (loadFluidId != 0) {
                Oil oil = oilMapper.selectById(loadFluidId);
                Fluid2 fluid2 = new Fluid2(oil);
                node.setLoadFluid(fluid2);
            }
        }
        this.network = network;
    }


    @Override
    public Network simulate() {

        init();

        Simulate simulate = new Simulate(network);
        simulate.Simulate();

        //对能耗进行计算，返回管网的能耗属性中
        EnergyConsumption energyConsumption = new EnergyConsumption(network);
        network.setEnergyConsumption(energyConsumption);

        return network;
    }

    //定节点流量，得到最优的温度出口温度
    @Override
    public Network optimizeTemperature() {

        init();

        List<Node> nodes = network.getNodeList();
        Node nodeT = null;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.getThermalType().equals("T")) {
                nodeT = node;
            }
        }

        Network networkOptimize = null;

        if (nodeT != null) {

            for (double startTem = nodeT.getTemperature(); startTem > 45; startTem--) {

                nodeT.setTemperature(startTem);

                Simulate simulate = new Simulate(network);

                simulate.Simulate();

                double[] temperature = simulate.getTemperature();
//                List temp = Arrays.asList(temperature);
//                double tem = (double) Collections.min(temp);
                Arrays.sort(temperature);

                if (temperature[0] < 36) {  //通过改变节点温度，来进行出口温度的优化
                    //通过改变节点流量，固定温度，进行流量优化
                    networkOptimize = network;
                    logger.info("在当前掺水量下最优的掺水温度为：" + startTem + "℃");
                    break;
                }
            }
        }
        return networkOptimize;
    }


    //给定出口掺输温度，给出最小掺水量
    @Override
    public Network optimizeQuantity() {


        init();

        List<Node> nodes = network.getNodeList();
        Node nodeQ = null;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.getOutPipe().size() == 0) {
                nodeQ = node;
                break;
            }
        }

        Network networkOptimize = null;

        if (nodeQ != null) {

            for (double startQuantityM = nodeQ.getFlowLoad(); startQuantityM < 0; startQuantityM++) {

                nodeQ.setFlowLoad(startQuantityM);

                Simulate simulate = new Simulate(network);

                simulate.Simulate();

                double[] temperature = simulate.getTemperature();
//                List temp = Arrays.asList(temperature);
//                double tem = (double) Collections.min(temp);
                Arrays.sort(temperature);

                if (temperature[0] < 36) {  //通过改变节点温度，来进行出口温度的优化
                    //通过改变节点流量，固定温度，进行流量优化
                    networkOptimize = network;
                    logger.info("在当前掺水量下最优的掺水量为：" + startQuantityM + "kg/s");
                    break;
                }
            }
        }
        return networkOptimize;
    }


    public Network optimizeEnergyLoss() {

        init();

        List<Node> nodes = network.getNodeList();
        Node nodeT = null;
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            if (node.getThermalType().equals("T")) {
                nodeT = node;
            }
        }

        Network networkOptimize = null;

        double lossSum;

        ArrayList<Double> quantityMAll = new ArrayList<>();
        ArrayList<Double> temperatureAll = new ArrayList<>();
        ArrayList<Double> lossSumAll = new ArrayList<>();

        double lowerStartTem = 20;

        /* 得到管网中的起点  假设只有一个起点 */
        if (nodeT != null) {


            for (double startQuantityM = -50; startQuantityM <= -20; startQuantityM++) {


                for (double startTem = 70; startTem > lowerStartTem; startTem--) {

                    /*掺输水温度和掺输水质量流量*/
                    quantityMAll.add(startQuantityM);
                    temperatureAll.add(startTem);

                    nodeT.setFlowLoad(startQuantityM);
                    nodeT.setTemperature(startTem);

                    /*进行稳态水热力计算*/
                    Simulate simulate = new Simulate(network);
                    simulate.Simulate();

                    /*进行稳态条件下总能耗计算*/
                    EnergyConsumption energyConsumption = new EnergyConsumption(network);

                    /*增加总能耗*/
                    lossSum = energyConsumption.getEnergyLossSum();
                    lossSumAll.add(lossSum);


                    /*设定不同掺水流量下的温度下限，循环模拟*/
                    double[] tem = simulate.getTemperature();
                    Arrays.sort(tem);
                    if (tem[0] < 36) {
                        lowerStartTem = startTem;
                    }

                }

            }
        }


        double minLossSum = Collections.min(lossSumAll);
        int index = lossSumAll.indexOf(minLossSum);
        double minQuantityM = quantityMAll.get(index);
        double minTemperature = temperatureAll.get(index);

        nodeT.setTemperature(minTemperature);
        nodeT.setFlowLoad(minQuantityM);

        Simulate simulate = new Simulate(network);
        simulate.Simulate();

        networkOptimize = network;

        List<ArrayList<Double>> energyLossMat = new ArrayList<>();
        energyLossMat.add(quantityMAll);
        energyLossMat.add(temperatureAll);
        energyLossMat.add(lossSumAll);

        networkOptimize.setEnergyLossMatrix(energyLossMat);

        return networkOptimize;
    }


    public List<ArrayList<Double>> energyLossMatrix() {
        Network network = optimizeEnergyLoss();
        return network.getEnergyLossMatrix();
    }

}
