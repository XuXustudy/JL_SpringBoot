package com.example.springboot.simulate;

import com.example.springboot.entity.Network;

import com.example.springboot.entity.Pipe;
import lombok.Data;

import java.util.List;

@Data
public class EnergyConsumption {

    private double[] fractionLoss;
    private double[] heatLoss;
    private double fractionLossSum;
    private double heatLossSum;
    private double energyLossSum;

    public EnergyConsumption(Network network) {

//        Simulate simulate = new Simulate(network);
//        simulate.Simulate();
        //进行官网的稳态计算，调用管道的能好计算，得到  摩阻损失  和  热量损失
        List<Pipe> pipes = network.getPipeList();

        double[] fractionLoss = new double[pipes.size()];
        double[] heatLoss = new double[pipes.size()];
        /**
         * 把泵的能耗加进去；加热炉的温降加进去
         *
         * 通过管道沿线的压降、通过管道沿线的温降；分别作为  压能损失 和  热能损失
         * */
        double fractionLossSum = 0;
        double heatLossSum = 0;
        double energyLossSum = 0;

        for (int i = 0; i < pipes.size(); i++) {

            Pipe pipe = pipes.get(i);
            fractionLoss[i] = pipe.getFrictionLoss();
            heatLoss[i] = pipe.getHeatLoss();
            fractionLossSum += fractionLoss[i];
            heatLossSum += heatLoss[i];
            energyLossSum = energyLossSum + fractionLoss[i] + heatLoss[i];
        }
        this.fractionLoss = fractionLoss;
        this.fractionLossSum = fractionLossSum;
        this.heatLoss = heatLoss;
        this.heatLossSum = heatLossSum;
        this.energyLossSum = energyLossSum;
    }

}
