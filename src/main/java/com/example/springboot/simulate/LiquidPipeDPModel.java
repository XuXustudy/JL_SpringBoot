package com.example.springboot.simulate;

import com.example.springboot.entity.Pipe;

public class LiquidPipeDPModel {
    private Pipe pipe;

    public LiquidPipeDPModel(Pipe pipe) {
        this.pipe = pipe;

    }

    public double getYita() {

        double quantityM = pipe.getQuantityM();


        double l = pipe.getLength();
        double inD = pipe.getOutsideDiameter() - 2 * pipe.getThickness();
        double A = inD * inD * Math.PI / 4;

        //得到平均温度和压力下的密度
        double startTem = pipe.getStartTemperature();
        double endTem = pipe.getEndTemperature();

        double aveTem = (startTem + endTem) / 2;

        //得到平均温度和含水率下的密度
        double density = pipe.getFluid().getDensity(aveTem);

        double Q = quantityM / density;
        double v = Q / A;

        //得到平均温度和压力下的粘度
        double miuL = pipe.getFluid().getViscosity(aveTem) * 1e-3;   //mPa*s

        double Re = Math.abs(v * inD * density / miuL);
        double epsilon = 2 * pipe.getRoughness() / inD;
        double Re1 = 59.5 / Math.pow(epsilon, 8 / 7);
        double Re2 = 665 - 765 * Math.log10(epsilon) / epsilon;

        double lamuda = 0;
        /**
         * 采用水力光滑区的  lamuda 计算公式进行计算
         * */
        lamuda = 0.3164 * Math.pow(Re, -0.25);

//        if (Re < 2000) {
//            lamuda = 64 / Re;
//        } else if (Re > 3000 && Re < Re1) {
//            lamuda = Math.pow(1 / (1.81 * Math.log10(Re) - 1.53), 2);
//            System.out.println(this.pipe.getId() + "使用水力光滑区进行计算");
//        } else if (Re > Re1 && Re < Re2) {
//            lamuda = 0.11 * Math.pow((pipe.getRoughness() / inD + 68 / Re), 0.25);
//            System.out.println(this.pipe.getId() + "使用混合摩擦区进行计算");
//        } else {
//            lamuda = 1 / Math.pow(1.74 - 2 * Math.log10(epsilon), 2);
//        }

        double yita = inD * 2 * density * Math.pow(A, 2) / (lamuda * l * Math.abs(quantityM));
        return yita;
    }
}
