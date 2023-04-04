package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Pipe;
import com.example.springboot.mapper.PipeMapper;
import com.example.springboot.service.IPipeService;
import org.springframework.stereotype.Service;

@Service
public class PipeServiceImpl extends ServiceImpl<PipeMapper, Pipe> implements IPipeService {

    private static final double g = 9.8;

    public double getTZ(Pipe pipe) {

        double K = pipe.getThermalConduct();
        double inD = pipe.getOutsideDiameter() - 2 * pipe.getThickness();

        double length = pipe.getLength();
        double quantityM = pipe.getQuantityM();

        double startPre = pipe.getStartPressure();
        double endPre = pipe.getEndPressure();

        double startTem = pipe.getStartTemperature();
        double endTem = pipe.getEndTemperature();

        double aveTem = (startTem + endTem) / 2;

        double C = pipe.getFluid().getC(aveTem);
        double density = pipe.getFluid().getDensity(aveTem);
        double i = (startPre - endPre) / (density * g);

        double a = K * Math.PI * inD / (quantityM * C);
        double b = g * i * quantityM / (K * Math.PI * inD);
        double groundTem = pipe.getGroundTemperature();

        double Tz = groundTem + b + (startTem - (groundTem + b)) * Math.exp(-a * length);

        return Tz;
    }


    public double getYita(Pipe pipe) {

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
        double miuL = pipe.getFluid().getViscosity(aveTem) * 1e-6;   //mPa*s

        double Re = v * inD * density / miuL;
        double epsilon = 2 * pipe.getRoughness() / inD;
        double Re1 = 59.5 / Math.pow(epsilon, 8 / 7);
        double Re2 = 665 - 765 * Math.log10(epsilon) / epsilon;

        double lamuda = 0;
        if (Re < 2000) {
            lamuda = 64 / Re;
        } else if (Re > 3000 && Re < Re1) {
            lamuda = Math.pow(1 / (1.81 * Math.log10(Re) - 1.53), 2);
        } else if (Re > Re1 && Re < Re2) {
            lamuda = 0.11 * Math.pow((pipe.getRoughness() / inD + 68 / Re), 0.25);
        } else {
            lamuda = 1 / Math.pow(1.74 - 2 * Math.log10(epsilon), 2);
        }

        double yita = inD * 2 * density * Math.pow(A, 2) / (lamuda * l * Math.abs(quantityM));
        return yita;
    }
}
