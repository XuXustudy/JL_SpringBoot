package com.example.springboot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.springboot.entity.Station;

/**
 * @author xuyihao
 * @date 2023-01-06
 * @apiNote
 */

public interface IOptimizeService extends IService<Station> {

    void Optimize118(double T);
}
