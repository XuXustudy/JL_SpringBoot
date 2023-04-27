package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.springboot.entity.Calresult;
import com.example.springboot.entity.ResultLength;
import com.example.springboot.entity.Station;
import com.example.springboot.mapper.StationMapper;
import com.example.springboot.service.IStationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyh
 * @since 2022-12-30
 */
@Service
public class StationServiceImpl extends ServiceImpl<StationMapper, Station> implements IStationService {
    @Autowired
    private  CalresultServiceImpl calresultService;

    @Resource
    private StationMapper stationMapper;

    @Override
    public List<Station> list118(){
        QueryWrapper<Station> Wrapper = new QueryWrapper<>();
        Wrapper.eq("facility","海118站");
        List<Station> list = stationMapper.selectList(Wrapper);
        return list;
    }

    @Override
    public double[] selectLength47() {

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        List<Station> selectList = stationMapper.selectList(Wrapper);
        int size = selectList.size();  //有相同的代码块在类里，可以把这部分代码封装成一个方法，提高代码可读性。
        double[] lenArr = new double[size];
        for (int i = 0; i < size; i++) {
            ResultLength oneResultLength = new ResultLength();
            oneResultLength.setId(selectList.get(i).getId());
            Double length = selectList.get(i).getPipeLength();
            lenArr[i] = length * 1000;
            oneResultLength.setResultLength(lenArr[i]);
            updateResult(oneResultLength);
        }
        Calresult calresult = new Calresult();
        calresult.setResultLength(Arrays.toString(lenArr));
        calresultService.insertOneCalresult(calresult);
        return lenArr;  //[3610 7270 2275 4036]
    }



    @Override
    public double[] CalGoInDia47() {

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        int size = stationMapper.selectList(Wrapper).size();
        double[] outDiaArr = new double[size];
        double[] thiArr = new double[size];
        double[] GoInDiaArr47 = new double[size];
        for (int i = 0; i < size; i++) {
            Double outDia = stationMapper.selectList(Wrapper).get(i).getPipeGoDiameter();
            Double thi = stationMapper.selectList(Wrapper).get(i).getPipeGoThickness();
            outDiaArr[i] = outDia;  //114.3、88.9、114.3、88.9
            thiArr[i] = thi;  //4、3.6、4、3.6
            GoInDiaArr47[i] = (outDiaArr[i] - 2 * thiArr[i]) / 1000;
        }
        return GoInDiaArr47;
    }

    @Override
    public double[] CalBackInDia47() {

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        int size = stationMapper.selectList(Wrapper).size();
        double[] outDiaArr = new double[size];
        double[] thiArr = new double[size];
        double[] BackInDiaArr47 = new double[size];
        for (int i = 0; i < size; i++) {
            Double outDia = stationMapper.selectList(Wrapper).get(i).getPipeBackDiameter();
            Double thi = stationMapper.selectList(Wrapper).get(i).getPipeBackThickness();
            outDiaArr[i] = outDia;  //168.3、114.3、168.3、114.3
            thiArr[i] = thi;  //4.5、4.0、4.5、4.0
            BackInDiaArr47[i] = (outDiaArr[i] - 2 * thiArr[i]) / 1000;
        }
        return BackInDiaArr47;
    }

    @Override
    public double[] CalGoDistribution47(double Q) {

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        int size = stationMapper.selectList(Wrapper).size();
        double[] lenArr = selectLength47();
        double[] GoInDiaArr = CalGoInDia47();
        double flowRateSum = 0;
        double[] flowRateArr = new double[size];
        for (int i = 0; i < GoInDiaArr.length; i++) {
            double inRate = Math.pow(GoInDiaArr[i] / GoInDiaArr[size - 1], 4.75);
            double flowRate = Math.pow((inRate * lenArr[size - 1] / lenArr[i]), 1 / 1.75);
            flowRateArr[i] = flowRate;
            flowRateSum += flowRate;
        }
        for (int i = 0; i < flowRateArr.length; i++) {
            flowRateArr[i] = flowRateArr[i] / flowRateSum;
        }
        double[] GoDistribution47 = new double[size];
        for (int i = 0; i < GoDistribution47.length; i++) {
            GoDistribution47[i] = flowRateArr[i] * Q / 3600 / 24;  //t/d→m3/s （10e3）KG/（24*3600）s/（KG/m3）ρ
        }
        return GoDistribution47;
    }

    @Override
    public double[] CalBackDistribution47(double Q) {

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        int size = stationMapper.selectList(Wrapper).size();
        double[] lenArr = selectLength47();
        double[] GoInDiaArr = CalGoInDia47();
        double flowRateSum = 0;
        double[] flowRateArr = new double[size];
        for (int i = 0; i < GoInDiaArr.length; i++) {
            double inRate = Math.pow(GoInDiaArr[i] / GoInDiaArr[size - 1], 4.75);
            double flowRate = Math.pow((inRate * lenArr[size - 1] / lenArr[i]), 1 / 1.75);
            flowRateArr[i] = flowRate;
            flowRateSum += flowRate;
        }
        for (int i = 0; i < flowRateArr.length; i++) {
            flowRateArr[i] = flowRateArr[i] / flowRateSum;
        }
        double[] qArr = new double[size];
        for (int i = 0; i < qArr.length; i++) {
            qArr[i] = flowRateArr[i] * Q / 3600 / 24;
        }
        double[] BackDistribution47 = new double[size];
        double[] SOilWell = new double[size];  //油井汇合至各计量间的流量
        for (int i = 0; i < size; i++) {
            SOilWell[i] = stationMapper.selectList(Wrapper).get(i).getFluidProduction();
            BackDistribution47[i] = qArr[i] + SOilWell[i] / 24 / 3600;
        }
        return BackDistribution47;
    }

    /**
     *
     * @param Q
     * @return
     * double miu = 6.24e-7;  //需要手动输入，目前先固定值
     * double e = 0.0084e-3;  //需要手动输入，目前先固定值
     */
    @Override
    public double[][] CalLambda47(double Q) {

        double miu = 6.24e-7;  //需要手动输入，目前先固定值
        double e = 0.0084e-3;  //需要手动输入，目前先固定值

        QueryWrapper<Station> Wrapper = new QueryWrapper<>();Wrapper.eq("facility","让47站");
        int size = stationMapper.selectList(Wrapper).size();
        double[] GoInDiaArr = CalGoInDia47();
        double[] BackInDiaArr = CalBackInDia47();
        double[] GoRe = new double[size];
        double[] BackRe = new double[size];
        double[] GoRe1 = new double[size];
        double[] GoRe2 = new double[size];
        double[] BackRe1 = new double[size];
        double[] BackRe2 = new double[size];
        for (int i = 0; i < size; i++) {
            GoRe[i] = 4 * CalGoDistribution47(Q)[i] / (Math.PI * GoInDiaArr[i] * miu);
            BackRe[i] = 4 * CalBackDistribution47(Q)[i] / (Math.PI * BackInDiaArr[i] * miu);
            GoRe1[i] = 59.5 / Math.pow((2 * e / GoInDiaArr[i]), 8.0 / 7.0);
            GoRe2[i] = (665 - 765 * Math.log10((2 * e / GoInDiaArr[i]))) / (2 * e / GoInDiaArr[i]);
            BackRe1[i] = 59.5 / Math.pow((2 * e / BackInDiaArr[i]), 8.0 / 7.0);
            BackRe2[i] = (665 - 765 * Math.log10((2 * e / BackInDiaArr[i]))) / (2 * e / BackInDiaArr[i]);
        }
        double[] Lambda1 = new double[size];  //去水管线lambda1
        double[] Lambda2 = new double[size];  //回油管线lambda2
        for (int i = 0; i < size; i++) {
            if (GoRe[i] <= 3000) {
                Lambda1[i] = 64.0 / GoRe[i];
            } else if (3000 < GoRe[i] && GoRe[i] <= GoRe1[i]) {
                Lambda1[i] = Math.pow(1.8 * Math.log10(GoRe[i]), -2);
            } else if (GoRe1[i] < GoRe[i] && GoRe[i] <= GoRe2[i]) {
                Lambda1[i] = 0.11 * Math.pow((e / GoInDiaArr[i] + 68 / GoRe[i]), 0.25);
            } else if (GoRe2[i] <= GoRe[i]) {
                Lambda1[i] = 0.11 * Math.pow(e / GoInDiaArr[i], 0.25);
            }
        }
        for (int i = 0; i < size; i++) {
            if (BackRe[i] <= 3000) {
                Lambda2[i] = 64.0 / BackRe[i];
            } else if (3000 < BackRe[i] && BackRe[i] <= BackRe1[i]) {
                Lambda2[i] = Math.pow(1.8 * Math.log10(BackRe[i]), -2);
            } else if (BackRe1[i] < BackRe[i] && BackRe[i] <= BackRe2[i]) {
                Lambda2[i] = 0.11 * Math.pow((e / BackInDiaArr[i] + 68 / BackRe[i]), 0.25);
            } else if (BackRe2[i] <= BackRe[i]) {
                Lambda2[i] = 0.11 * Math.pow(e / BackInDiaArr[i], 0.25);
            }
        }
        double[][] LambdaArr47 = new double[2][];
        LambdaArr47[0] = Lambda1;
        LambdaArr47[1] = Lambda2;
        return LambdaArr47;
    }

    /**\
     *
     * @param Q
     * @param a
     * @return
     * int rou = 1000;//需要手动输入，目前先固定值
     */
    @Override
    public double[] CalDeltaPressure47(double Q, double a) {

        int rou = 1000;//需要手动输入，目前先固定值

        double[][] lambdas = CalLambda47(Q);
        double[] lenArr = selectLength47();
        double[] GoInDiaArr = CalGoInDia47();
        double[] BackInDiaArr = CalBackInDia47();
        double[] v1 = new double[lenArr.length];
        double[] v2 = new double[lenArr.length];
        for (int y = 0; y < lenArr.length; y++) {
            v1[y] = 8 * lambdas[0][y] * rou * lenArr[y] * Math.pow(CalGoDistribution47(Q)[y] * a, 2) / (Math.pow(Math.PI, 2) *
                    Math.pow(GoInDiaArr[y], 5)) / 1000000;  //单位MPa
        }
        for (int x = 0; x < lenArr.length; x++) {
            v2[x] = 8 * lambdas[1][x] * rou * lenArr[x] * Math.pow(CalBackDistribution47(Q)[x] * a, 2) / (Math.pow(Math.PI, 2) *
                    Math.pow(BackInDiaArr[x], 5)) / 1000000;  //单位MPa
        }
        double[] PressureArr47 = new double[lenArr.length];
        for (int i = 0; i < lenArr.length; i++) {
            PressureArr47[i] = v1[i] + v2[i];
        }
        return PressureArr47;
    }

    @Override
    public int updateResult(ResultLength resultLength) {
        UpdateWrapper<Station> Wrapper = new  UpdateWrapper<>();
        Wrapper.eq("id",resultLength.getId()).set("result_length",resultLength.getResultLength());
        int i = stationMapper.update(null,Wrapper);
        return 0;
    }
}
