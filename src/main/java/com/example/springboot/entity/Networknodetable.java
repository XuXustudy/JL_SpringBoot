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
@TableName("networknodetable")
@ApiModel(value = "Networknodetable对象", description = "")
public class Networknodetable implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("管网节点ID")
    @TableId(value = "ID", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("节点名字")
    private String nodeName;

    @ApiModelProperty("节点经度")
    private String lat;

    @ApiModelProperty("节点纬度")
    private String lng;

    @ApiModelProperty("所属管网的ID")
    private Integer uid;


}
