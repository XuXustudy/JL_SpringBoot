package com.example.springboot.service.impl;

import com.example.springboot.entity.Calresult;
import com.example.springboot.mapper.CalresultMapper;
import com.example.springboot.service.ICalresultService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-04-06
 */
@Service
public class CalresultServiceImpl extends ServiceImpl<CalresultMapper, Calresult> implements ICalresultService {

    @Resource
    private CalresultMapper calresultMapper;

    @Override
    public int insertOneCalresult(Calresult calresult) {
        calresultMapper.insert(calresult);
        return 0;
    }
}
