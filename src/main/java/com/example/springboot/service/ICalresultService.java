package com.example.springboot.service;

import com.example.springboot.entity.Calresult;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyh
 * @since 2023-04-06
 */
public interface ICalresultService extends IService<Calresult> {
    int insertOneCalresult(Calresult calresult);
}
