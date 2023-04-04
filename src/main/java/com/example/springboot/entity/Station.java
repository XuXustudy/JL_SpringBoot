package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 *
 * </p>
 *
 * @author xyh
 * @since 2022-12-30
 */
@Data
@ApiModel(value = "Station对象", description = "")

public class Station implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private String facility;

    private String room;

    private Double pipeLength;

    private Double pipeGoDiameter;

    private Double pipeGoThickness;

    private Double pipeBackDiameter;

    private Double pipeBackThickness;

    private Double fluidProduction;

    private Double waterCut;


}
