package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 *
 * </p>
 *
 * @author xyh
 * @since 2023-03-10
 */
@Data
@TableName("fluid")
@ApiModel(value = "Fluid对象", description = "")
public class Fluid implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("外输物流名称")
    private String name;

    @ApiModelProperty("凝点℃")
    private Double condensationPoint;

    @ApiModelProperty("20℃，密度kg/m3")
    private Double density;

    @ApiModelProperty("比热容J/(kg·℃)")
    private Double c;

    @ApiModelProperty("动力粘度mPa·s")
    private Double viscosity;

    @ApiModelProperty("含水率")
    private Double waterCut;

}


