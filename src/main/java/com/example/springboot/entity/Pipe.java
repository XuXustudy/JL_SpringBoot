package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

@Data
@TableName("pipe")
public class Pipe {

    private int id;

    private int pipeId;

    private String pipeName;
    private double length;
    private double outsideDiameter;
    private double thickness;
    private double roughness;

    private double thermalConduct;

    private int projectId;

    private double groundTemperature;
    private double startTemperature;
    private double endTemperature;
    private double startPressure;
    private double endPressure;

    private int startNumb;

    private int endNumb;

    private double quantityM;     //质量流量

    @JsonIgnore
    @TableField(exist = false)
    private Fluid2 fluid;
    @JsonIgnore
    @TableField(exist = false)
    private Node startNode;
    @JsonIgnore
    @TableField(exist = false)
    private Node endNode;

    private static final double g = 9.8;  //kg/(m*s2）

    public Pipe() {

    }

    public Pipe(double length, double outsideDiameter, double thickness, double thermalConduct) {
        this.length = length;
        this.outsideDiameter = outsideDiameter;
        this.thickness = thickness;
        this.thermalConduct = thermalConduct;
    }

    /**
     * 计算管道的热量损失， 单位  W(瓦)
     * heatLoss = C*m*deltT
     */
    public double getHeatLoss() {
        double aveTem = (startTemperature + endTemperature) / 2;
        double aveC = fluid.getC(aveTem);
        double heatLoss = aveC * quantityM * (startTemperature - endTemperature);
        return heatLoss;
    }

    /**
     * 计算管路中的摩阻损失, 单位  W（瓦）
     */
    public double getFrictionLoss() {
        double aveTem = (startTemperature + endTemperature) / 2;
        double aveDensity = fluid.getDensity(aveTem);
        double frictionLoss = (startPressure - endPressure) / aveDensity * quantityM;
        return frictionLoss;
    }

}
