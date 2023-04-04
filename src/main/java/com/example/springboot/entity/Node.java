package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;


import java.util.ArrayList;
import java.util.List;

@Data
@TableName("node")
public class Node {

    @TableId(value = "id", type = IdType.AUTO)
    private int id;

    private int nodeId;

    private String nodeName;

    private double flowLoad;     //质量载荷, m^3/s

    private int loadFluidId;     //流体的id，id=0为纯水；id!=0为油品+水

    private int projectId;

    private double pressure;

    private double temperature;        //节点温度

    private String hydraulicType;      //节点的边界条件类型;类型为'Q'或者’P‘;对于水力类型为 Q 的点，需指定流量 Q 的含水率


    private String thermalType;        //节点的温度类型，T或NT

    @JsonIgnore
    @TableField(exist = false)
    private List<Pipe> inPipe;
    @JsonIgnore
    @TableField(exist = false)
    private List<Pipe> outPipe;
    @JsonIgnore
    @TableField(exist = false)
    private List<Pipe> pipes;
    @JsonIgnore
    @TableField(exist = false)
    private List<Integer> sequenceOfT;        //求解温度T的顺序
    @JsonIgnore
    @TableField(exist = false)
    private Fluid2 loadFluid;    //节点载荷的  流体类型


    public Node() {
        pipes = new ArrayList<>();
        inPipe = new ArrayList<>();
        outPipe = new ArrayList<>();
        hydraulicType = "Q";
        flowLoad = 0;
        pressure = 0;
    }

    public void addPipe(Pipe pipe) {
        pipes.add(pipe);
    }

    public void addInPipe(Pipe pipe) {
        inPipe.add(pipe);
    }

    public void addOutPipe(Pipe pipe) {
        outPipe.add(pipe);
    }

    @Override
    public String toString() {
        return nodeName;
    }
}
