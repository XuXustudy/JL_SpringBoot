package com.example.springboot.service.impl;

import com.example.springboot.entity.Exportfluid;
import com.example.springboot.mapper.ExportfluidMapper;
import com.example.springboot.service.IExportfluidService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
@Service
public class ExportfluidServiceImpl extends ServiceImpl<ExportfluidMapper, Exportfluid> implements IExportfluidService {

    @Override  //油井产物的运动粘度和温度曲线m2/s
    public double ViscosityTemperatureCurve(double tem) {
        return 0.0098 * Math.pow(tem, -1.741);
    }

    @Override  //油井产物的密温曲线
    public double DensityTemperatureCurve(double tem) {
        return -0.719 * tem + 855.38;
    }

    @Override  //水的密温曲线
    public double WaterDensityTemperatureCurve(double tem) {
        return -0.0036 * Math.pow(tem, 2) - 0.0697 * tem + 1000.5;
    }
}
