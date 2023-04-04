package com.example.springboot.entity;

import lombok.Data;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.event.MouseInputListener;

@Data
public class Fluid2 {

    private double waterCut;

    private Oil oil;

    private Water water;

    private static final Logger logger = LoggerFactory.getLogger(Fluid2.class);

    public Fluid2(Oil oil) {
        this.oil = oil;
        this.waterCut = oil.getWaterCut();
        this.water = new Water();
    }

    public Fluid2() {
        this.water = new Water();
        this.waterCut = 1;
    }


    public double getDensity(double temperature) {
        double oilDensity = 0;
        if (waterCut != 1) {
            oilDensity = oil.getDensity(temperature);
        }
        double waterDensity = water.getDensity(temperature);

        return oilDensity * (1 - waterCut) + waterDensity * waterCut;
    }


    public double getViscosity(double temperature) {
        /** 以水为连续相，得到  油水混合物的粘度
         *  Taylor 公式
         *  */
        double waterMiu = water.getViscosity(temperature);
        double oilMiu = 0;
        if (waterCut != 1) {
            oilMiu = oil.getViscosity(temperature);
        }

        double mium = waterMiu * (1 + 2.5 * (oilMiu + 0.4 * waterMiu) / (oilMiu + waterMiu) * waterCut);


        return mium;
    }

    /**
     * @param temperature 温度℃
     * @return mixC 混合比热容  J/(kg*K)
     */

    public double getC(double temperature) {
        double oilC = 0;
        if (waterCut != 1) {
            oilC = oil.getC(temperature) ;
        }
        double waterC = water.getC(temperature);

        double mixC = oilC * (1 - waterCut) + waterC * waterCut;
        return mixC;
    }

}
