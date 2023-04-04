package com.example.springboot.entity;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@JsonSerialize
public class Water implements FluidPar {

    private static final Logger logger = LoggerFactory.getLogger(Water.class);
    @Override
    public double getDensity(double temperature) {
        double density = -0.0055 * temperature * temperature + 0.0228 * temperature + 999.99;
        return density;
    }

    /**
     * @param temperature ℃
     * @return miu 纯水不同温度下的粘度 mPa*s
     */
    @Override
    public double getViscosity(double temperature) {

        double miu = Math.exp(1.003 - (1.479 * 1E-2 * (1.8 * temperature + 32)) +
                (1.982* 1E-5* Math.pow(1.8 * temperature + 32, 2)));

        return miu;
    }

    /**
     * @param temperature ℃
     * @return 返回不同温度下水的比热容
     * 桌面文件  水的   比热容-温度  关系  拟合得到的比热容-温度曲线
     */
    @Override
    public double getC(double temperature) {
//        double CWater = (5E-10 * Math.pow(temperature, 4) - 2E-07 * Math.pow(temperature, 3)
//                + 4E-05 * Math.pow(temperature, 2) - 0.0026 * temperature + 4.2274) * 1000;
        double CWater = 4172;
        return CWater;
    }

    /**
     * 通过拟合关系曲线，来确定水的粘度
     *
     * @param temAndVis 温度-粘度  对应的数组
     */
    @Override
    public double[] getViscosityPar(double[][] temAndVis) {
        return new double[0];
    }

    @Override
    public double[] getCPar(double[][] temAndC) {
        return new double[0];
    }
}
