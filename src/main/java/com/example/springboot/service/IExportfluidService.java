package com.example.springboot.service;

import com.example.springboot.entity.Exportfluid;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
public interface IExportfluidService extends IService<Exportfluid> {

    double ViscosityTemperatureCurve(double tem); //粘温曲线

    double DensityTemperatureCurve(double tem); //密温曲线

    double WaterDensityTemperatureCurve(double tem);
}
