package com.example.springboot.simulate;

import com.example.springboot.entity.Pipe;

public class LiquidDTModel {

    private Pipe pipe;
    private static final double g = 9.8;

    public LiquidDTModel(Pipe pipe) {
        this.pipe = pipe;
    }

    public double getTZ() {

        double K = pipe.getThermalConduct();
        double outD = pipe.getOutsideDiameter();

        double length = pipe.getLength();
        double quantityM = pipe.getQuantityM();

        double startPre = pipe.getStartPressure();
        double endPre = pipe.getEndPressure();

        double startTem = pipe.getStartTemperature();
        double endTem = pipe.getEndTemperature();

        double aveTem = (startTem + endTem) / 2;

        double C = pipe.getFluid().getC(aveTem);
        double density = pipe.getFluid().getDensity(aveTem);
        double i = (startPre - endPre) / (density * g) / length;

        double a = K * Math.PI * outD / (quantityM * C);

        double b = g * i * quantityM / (K * Math.PI * outD);
        double groundTem = pipe.getGroundTemperature();

        double Tz = groundTem + b + (startTem - (groundTem + b)) * Math.exp(-a * length);

        return Tz;
    }


}
