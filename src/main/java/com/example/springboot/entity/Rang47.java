package com.example.springboot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
 * @since 2023-02-28
 */
@Getter
@Setter
  @ApiModel(value = "Rang47对象", description = "")
public class Rang47 implements Serializable {

    private static final long serialVersionUID = 1L;

      @TableId(value = "id", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("总掺输水流量")
      private Double totalMixedWaterFlow;

      @ApiModelProperty("去站外掺水阀组汇管温度")
      private Double valveManifoldTemperature;

      @ApiModelProperty("去站外掺水阀组汇管压力")
      private Double valveManifoldPressure;

      @ApiModelProperty("站外来液进站温度")
      private Double inletTemperature;

      @ApiModelProperty("站外来液进站压力")
      private Double inletPressure;

      @ApiModelProperty("掺输泵进口温度")
      private Double pumpInletTemperature;

      @ApiModelProperty("掺输泵进口压力")
      private Double pumpInletPressure;

      @ApiModelProperty("掺输泵出口温度")
      private Double pumpOutletTemperature;

      @ApiModelProperty("掺输泵出口压力")
      private Double pumpOutletPressure;

      @ApiModelProperty("加热炉进口温度")
      private Double furnaceInletTemperature;

      @ApiModelProperty("加热炉进口压力")
      private Double furnaceInletPressure;

      @ApiModelProperty("加热炉出口温度")
      private Double furnaceOutletTemperature;

      @ApiModelProperty("加热炉出口压力")
      private Double furnaceOutletPressure;


}
