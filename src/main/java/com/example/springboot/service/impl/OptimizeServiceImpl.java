package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Exportfluid;
import com.example.springboot.entity.Exportstation;
import com.example.springboot.entity.Station;
import com.example.springboot.mapper.ExportfluidMapper;
import com.example.springboot.mapper.ExportstationMapper;
import com.example.springboot.mapper.StationMapper;
import com.example.springboot.service.IOptimizeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuyihao
 * @date 2023-01-06
 * @apiNote
 */
@Service
public class OptimizeServiceImpl extends ServiceImpl<StationMapper, Station> implements IOptimizeService {

    @Resource
    private ExportfluidMapper exportfluidMapper;

    @Resource
    private ExportstationMapper exportstationMapper;

    @Resource
    private ExportfluidServiceImpl exportfluidService;

    @Resource
    private ExportstationServiceImpl exportstationService;

    /**
     * @param T 终点温度T设置
     */
    @Override
    public void Optimize118(double T) {

        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "海118站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "海118站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);

        double T0 = T + 15;
        double diameter = listSta.get(0).getDiameter() / 1000; //0.159m
        double length = listSta.get(0).getLength() * 1000; //3400m
        double G = listFlu.get(0).getExportFlow() * 1000 / 3600;  //单位kg/s
        Double c = listFlu.get(0).getC(); //3050
        for (double i = 0; i < 15; i++) {
            if (-3 + (T0 -(-3)) * Math.pow(Math.E, -exportstationService.CalExportK118() * Math.PI * exportstationService.CalExportD118() * length / G / c) > T) {
                //加热炉能耗表示
                double Ne1 = c * G * (T0 - T) / 1000;
                //泵能耗表示
                double averageT = T0 / 3 + T * 2 / 3;

                Double pumpInletPressure = listSta.get(0).getPumpInletPressure();
                Double endPressure = listSta.get(0).getEndPressure();
                double OilDensity = exportfluidService.DensityTemperatureCurve(averageT);
                double waterDensity = exportfluidService.WaterDensityTemperatureCurve(averageT);
                Double waterCut = listFlu.get(0).getWaterCut();
                double averageViscosity = exportfluidService.ViscosityTemperatureCurve(averageT); //平均粘度
                double averageDensity = waterCut * waterDensity + (1 - waterCut) * OilDensity; //平均密度
                Double density = listFlu.get(0).getDensity();
                double Q = G / density;
                double L = listSta.get(0).getLength() * 1000;

                double Ne2 = (endPressure - pumpInletPressure + averageDensity * 9.8 * 0.0246 *
                        Math.pow(Q, 1.75) * Math.pow(averageViscosity, 0.25)
                        * L / Math.pow(exportstationService.CalExportD118(), 4.75)) * Q / 1000;

                //总能耗
                double Ne = Ne1 + Ne2;
                T0 = T0 - 1;
                System.out.println("第" + i + "次能耗：" + Ne);
            }else {
                System.out.println("终点温度不符合");
            }
        }
    }
}
