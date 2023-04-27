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
 * @since 2023-04-26
 */
@Getter
@Setter
  @TableName("pipedatatable")
@ApiModel(value = "Pipedatatable对象", description = "")
public class Pipedatatable implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("管道数据ID")
        @TableId(value = "ID", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("管道长度")
      private Double length;

      @ApiModelProperty("管道外径")
      private Double outDiameter;

      @ApiModelProperty("管道壁厚")
      private Double wallThickness;

      @ApiModelProperty("管道粗糙度")
      private Double roughness;

      @ApiModelProperty("计算说明")
      private String statement;


}
