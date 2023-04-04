package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.springboot.entity.Exportfluid;
import com.example.springboot.entity.Exportstation;
import com.example.springboot.mapper.ExportfluidMapper;
import com.example.springboot.mapper.ExportstationMapper;
import com.example.springboot.service.IExportstationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
@Service
public class ExportstationServiceImpl extends ServiceImpl<ExportstationMapper, Exportstation> implements IExportstationService {

    @Resource
    private ExportstationMapper exportstationMapper;

    @Resource
    private ExportfluidMapper exportfluidMapper;

    @Resource
    private ExportfluidServiceImpl exportfluidService;

    /**
     * 海118站外输流程，求总传热系数K
     */
    @Override
    public double CalExportK118() {
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "海118站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);

        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "海118站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature(); //45
        Double endTemperature = listSta.get(0).getEndTemperature(); //36
        Double ambientTemperature = listSta.get(0).getAmbientTemperature(); //-19
        double diameter = listSta.get(0).getDiameter() / 1000; //0.159m
        double length = listSta.get(0).getLength() * 1000; //3400m
        Double G = listFlu.get(0).getExportFlow() * 1000 / 3600;  //4.24kg/s
        Double c = listFlu.get(0).getC(); //3050

        double K118 = G * c * Math.log((startTemperature - ambientTemperature) / (endTemperature - ambientTemperature))
                / Math.PI / diameter / length;
        return K118;
    }

    /**
     * 海118站外输流程，求实际管径d
     */
    @Override
    public double CalExportD118() {

        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "海118站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "海118站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature();
        Double endTemperature = listSta.get(0).getEndTemperature();
        double averageT = startTemperature / 3 + 2 * endTemperature / 3;
        double OilDensity = exportfluidService.DensityTemperatureCurve(averageT);
        double waterDensity = exportfluidService.WaterDensityTemperatureCurve(averageT);
        Double waterCut = listFlu.get(0).getWaterCut();
        double averageViscosity = exportfluidService.ViscosityTemperatureCurve(averageT); //平均粘度
        double averageDensity = waterCut * waterDensity + (1 - waterCut) * OilDensity; //平均密度

        Double startPressure = listSta.get(0).getStartPressure();
        Double endPressure = listSta.get(0).getEndPressure();
        double L = listSta.get(0).getLength() * 1000;
        Double G = listFlu.get(0).getExportFlow() * 1000 / 3600;
        Double density = listFlu.get(0).getDensity();
        double Q = G / density;

        //处于水力光滑区
        double d118 = Math.pow((averageDensity * 9.8 * 0.0246 * Math.pow(Q, 1.75) * Math.pow(averageViscosity, 0.25) * L) /
                ((startPressure - endPressure) * 1000000), 1 / 4.75);


        return d118;
    }

    /**
     * 新民6站外输流程，求总传热系数K
     */
    @Override
    public double CalExportK6() {
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "新6站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);

        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "新6站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature();
        Double endTemperature = listSta.get(0).getEndTemperature();
        Double ambientTemperature = listSta.get(0).getAmbientTemperature();
        Double G = listFlu.get(0).getExportFlow() * 1000 / 3600;
        Double c = listFlu.get(0).getC();
        Double diameter = listSta.get(0).getDiameter();
        Double length = listSta.get(0).getLength();

        double K6 = G * c * Math.log((startTemperature - ambientTemperature) / (endTemperature - ambientTemperature))
                / Math.PI / diameter / length;
        return K6;
    }

    /**
     * 新民6站外输流程，求实际管径d
     */
    @Override
    public double CalExportD6() {

        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "新6站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "新6站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature();
        Double endTemperature = listSta.get(0).getEndTemperature();
        double averageT = startTemperature / 3 + 2 * endTemperature / 3;
        double OilDensity = exportfluidService.DensityTemperatureCurve(averageT);
        double waterDensity = exportfluidService.WaterDensityTemperatureCurve(averageT);
        Double waterCut = listFlu.get(0).getWaterCut();
        double averageViscosity = exportfluidService.ViscosityTemperatureCurve(averageT); //平均粘度
        double averageDensity = waterCut * waterDensity + (1 - waterCut) * OilDensity; //平均密度

        Double startPressure = listSta.get(0).getStartPressure();
        Double endPressure = listSta.get(0).getEndPressure();
        double L = listSta.get(0).getLength() * 1000;
        Double G = listFlu.get(0).getExportFlow();
        Double density = listFlu.get(0).getDensity();
        double Q = G * 1000 / 3600 / density;

        //处于水力光滑区
        double d6 = Math.pow((averageDensity * 9.8 * 0.0246 * Math.pow(Q, 1.75) * Math.pow(averageViscosity, 0.25) * L) /
                ((startPressure - endPressure) * 1000000), 1 / 4.75);

        return d6;
    }

    /**
     * 让47站外输流程，求总传热系数K
     */
    @Override
    public double CalExportK47() {
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "让47站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);

        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "让47站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature();
        Double endTemperature = listSta.get(0).getEndTemperature();
        Double ambientTemperature = listSta.get(0).getAmbientTemperature();
        Double G = listFlu.get(0).getExportFlow() * 1000 / 3600;
        Double c = listFlu.get(0).getC();
        Double diameter = listSta.get(0).getDiameter();
        Double length = listSta.get(0).getLength();

        double K47 = G * c * Math.log((startTemperature - ambientTemperature) / (endTemperature - ambientTemperature))
                / Math.PI / diameter / length;
        return K47;
    }

    /**
     * 让47站外输流程，求实际管径d
     */
    @Override
    public double CalExportD47() {

        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "让47站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        QueryWrapper<Exportfluid> wrapper = new QueryWrapper<>();
        wrapper.eq("name", "让47站");
        List<Exportfluid> listFlu = exportfluidMapper.selectList(wrapper);

        Double startTemperature = listSta.get(0).getStartTemperature();
        Double endTemperature = listSta.get(0).getEndTemperature();
        double averageT = startTemperature / 3 + 2 * endTemperature / 3;
        double OilDensity = exportfluidService.DensityTemperatureCurve(averageT);
        double waterDensity = exportfluidService.WaterDensityTemperatureCurve(averageT);
        Double waterCut = listFlu.get(0).getWaterCut();
        double averageViscosity = exportfluidService.ViscosityTemperatureCurve(averageT); //平均粘度
        double averageDensity = waterCut * waterDensity + (1 - waterCut) * OilDensity; //平均密度

        Double startPressure = listSta.get(0).getStartPressure();
        Double endPressure = listSta.get(0).getEndPressure();
        double L = listSta.get(0).getLength() * 1000;
        Double G = listFlu.get(0).getExportFlow() * 1000 / 3600 ;
        Double density = listFlu.get(0).getDensity();
        double Q = G / density;

        //处于水力光滑区
        double d47 = Math.pow((averageDensity * 9.8 * 0.0246 * Math.pow(Q, 1.75) * Math.pow(averageViscosity, 0.25) * L) /
                ((startPressure - endPressure) * 1000000), 1 / 4.75);

        return d47;
    }

    /**
     * 计算管输效率118
     */
    @Override
    public double calExportEfficiency118() {
        double d118 = CalExportD118();
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "海118站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        double diameter118 = listSta.get(0).getDiameter() / 1000; //0.159m

        return d118/diameter118;
    }

    /**
     * 计算管输效率6
     */
    @Override
    public double calExportEfficiency6() {
        double d6 = CalExportD6();
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "新6站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        double diameter6 = listSta.get(0).getDiameter() / 1000;

        return d6/diameter6;
    }

    /**
     * 计算管输效率47
     */
    @Override
    public double calExportEfficiency47() {
        double d47 = CalExportD47();
        QueryWrapper<Exportstation> Wrapper = new QueryWrapper<>();
        Wrapper.eq("start_station", "让47站");
        List<Exportstation> listSta = exportstationMapper.selectList(Wrapper);
        double diameter47 = listSta.get(0).getDiameter() / 1000;

        return d47/diameter47;
    }
}
