package com.example.springboot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.springboot.entity.Pipe;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PipeMapper extends BaseMapper<Pipe> {

}
