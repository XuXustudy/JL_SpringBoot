package com.example.springboot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Station;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyh
 * @since 2022-12-30
 */
public interface IStationService extends IService<Station> {

    List<Station> list118();

    double[] selectLength47();

    double[] CalGoInDia47();

    double[] CalBackInDia47();

    double[] CalGoDistribution47(double Q);

    double[] CalBackDistribution47(double Q);

    double[][] CalLambda47(double Q);

    double[] CalDeltaPressure47(double Q, double a);
}
