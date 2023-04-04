package com.example.springboot;

import com.example.springboot.service.impl.NodeServiceImpl;
import com.example.springboot.service.impl.OptimizeServiceImpl;
import com.example.springboot.service.impl.StationServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Arrays;

@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    private StationServiceImpl stationService;

    @Resource
    private OptimizeServiceImpl optimizeService;

    @Resource
    private NodeServiceImpl nodeimpl;

    @Test
    public void testZero() {
        double[] doubles = stationService.selectLength47();
        System.out.println(Arrays.toString(doubles));
    }

    @Test
    public void testOne() {
        optimizeService.Optimize118(35);
    }


    @Test
    public void find() {
        System.out.println(nodeimpl.getById(1));

    }
}
