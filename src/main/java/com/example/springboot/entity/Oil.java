package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
@Setter
@TableName("oil")
public class Oil implements FluidPar {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;  //油品id

    private String oilName;

    private double waterCut;  //含水率

    private double density20;  //脱水原油的密度

    private double oilTemperature;  //来油温度

    private static final Logger logger = LoggerFactory.getLogger(Oil.class);

    @TableField(exist = false)
    private double viscosity20;

    public Oil() {
    }

    public Oil(double density20, double waterCut) {

        this.density20 = density20;
        this.waterCut = waterCut;
        this.viscosity20 = Math.pow(0.658 * density20 * density20 / (0.866 - density20 * density20), 2);

    }

    @Override
    public double getDensity(double temperature) {  //返回kg/m^3
        double density = 0;
        double alpha = 0;
        if (temperature > 20 && temperature < 120) {

            if (density20 >= 780 && density20 < 860) {
                alpha = (3.083 - 2.638 * 1E-3 * density20) * 1E-3;
            } else if (density20 >= 860 && density20 < 960) {
                alpha = (2.513 - 1.975 * 1E-3 * density20) * 1E-3;
            } else {
                alpha = 0;
            }
        }
        density = density20 / (1 + alpha * (temperature - 20));
        return density;
    }

    @Override
    public double getViscosity(double temperature) {  //  mPa*s； 温度  ℃
        double relativeDen = getDensity(temperature) / 1000;
        double z = 3.0324 - 0.02023 * (141.5 - 131.5 * relativeDen) / relativeDen;
        double y = Math.pow(10, z);
        double x = y * Math.pow((1.8 * temperature + 32), -1.163);
        double miu = Math.pow(10, x) - 1;

        return miu;
    }

    @Override
    public double getC(double temperature) {
        double density15 = getDensity(15);    //15摄氏度下的原油密度  kg/m^3
        double Co = (1.678 + 3.39 * 1E-3 * temperature) / Math.pow(density15 / 1000, 0.5) * 1000;
        return Co;
    }

    @Override
    public double[] getViscosityPar(double[][] temAndVis) {
        return new double[0];
    }

    @Override
    public double[] getCPar(double[][] temAndC) {
        return new double[0];
    }
}
