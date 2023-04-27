package com.example.springboot.mapper;

import com.example.springboot.entity.Networknodetable;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xyh
 * @since 2023-04-26
 */
@Mapper
public interface NetworknodetableMapper extends BaseMapper<Networknodetable> {

    //查询管网节点表中对应该管网的节点数量
    @Select("SELECT COUNT(*) FROM networknodetable")
    int Counts();
}
