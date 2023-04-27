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
  @TableName("connectionrelationshiptable")
@ApiModel(value = "Connectionrelationshiptable对象", description = "")
public class Connectionrelationshiptable implements Serializable {

    private static final long serialVersionUID = 1L;

      @ApiModelProperty("连接关系ID")
        @TableId(value = "ID", type = IdType.AUTO)
      private Integer id;

      @ApiModelProperty("起点ID")
      private Integer startId;

      @ApiModelProperty("终点ID")
      private Integer endId;


}
