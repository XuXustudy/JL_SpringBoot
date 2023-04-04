package com.example.springboot.service.impl;

import com.example.springboot.entity.Fluid;
import com.example.springboot.mapper.FluidMapper;
import com.example.springboot.service.IFluidService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-03-10
 */
@Service
public class FluidServiceImpl extends ServiceImpl<FluidMapper, Fluid> implements IFluidService {

}
