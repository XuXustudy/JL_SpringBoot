package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
@Getter
@Setter
@TableName("exportstation")
@ApiModel(value = "Exportstation对象", description = "")
public class Exportstation implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外输起点接转站")
    private String startStation;

    @ApiModelProperty("起点温度℃")
    private Double startTemperature;

    @ApiModelProperty("终点温度℃")
    private Double endTemperature;

    @ApiModelProperty("起点压力MPa")
    private Double startPressure;

    @ApiModelProperty("终点压力MPa")
    private Double endPressure;

    @ApiModelProperty("管长km")
    private Double length;

    @ApiModelProperty("管外径mm")
    private Double diameter;

    @ApiModelProperty("环境温度℃")
    private Double ambientTemperature;

    @ApiModelProperty("泵进口压力MPa")
    private Double pumpInletPressure;
}
