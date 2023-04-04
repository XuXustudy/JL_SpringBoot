package com.example.springboot.service.impl;

import com.example.springboot.entity.Oil;
import com.example.springboot.mapper.OilMapper;
import com.example.springboot.service.IOilService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-03-30
 */
@Service
public class OilServiceImpl extends ServiceImpl<OilMapper, Oil> implements IOilService {

}
