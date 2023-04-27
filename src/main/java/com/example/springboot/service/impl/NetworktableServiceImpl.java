package com.example.springboot.service.impl;

import com.example.springboot.entity.Networktable;
import com.example.springboot.mapper.NetworktableMapper;
import com.example.springboot.service.INetworktableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-04-26
 */
@Service
public class NetworktableServiceImpl extends ServiceImpl<NetworktableMapper, Networktable> implements INetworktableService {

}
