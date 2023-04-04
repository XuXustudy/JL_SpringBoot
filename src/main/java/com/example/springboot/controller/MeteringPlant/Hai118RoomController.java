package com.example.springboot.controller.MeteringPlant;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Station;
import com.example.springboot.service.IStationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/hai118")
public class Hai118RoomController {

    @Resource
    private IStationService stationService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Station station) {
        return stationService.saveOrUpdate(station);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id) {
        return stationService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids) {
        return stationService.removeBatchByIds(ids);
    }

    //查询数据接口
    @GetMapping
    public List<Station> findAll() {
        return stationService.list118();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Station findOne(@PathVariable Integer id) {
        return stationService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Station> findPage(@RequestParam Integer pageNum,
                                  @RequestParam Integer pageSize,
                                  @RequestParam(defaultValue = "") String facility,
                                  @RequestParam(defaultValue = "") String room) {
        QueryWrapper<Station> queryWrapper = new QueryWrapper<>();
        if (!"".equals(facility)) {
            queryWrapper.like("facility", facility);
        }
        if (!"".equals(room)) {
            queryWrapper.like("room", room);
        }
        queryWrapper.orderByDesc("id");
        return stationService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    //导出接口
    @GetMapping("/export")
    public void export(HttpServletResponse response) throws Exception {
        // 从数据库查询出所有的数据
        List<Station> list = stationService.list118();
        // 通过工具类创建writer
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 自定义标题别名
        writer.addHeaderAlias("facility", "接转站");
        writer.addHeaderAlias("room", "计量间");
        writer.addHeaderAlias("pipeLength", "管长km");
        writer.addHeaderAlias("pipeGoDiameter", "去水管径mm");
        writer.addHeaderAlias("pipeGoThickness", "去水壁厚mm");
        writer.addHeaderAlias("pipeBackDiameter", "回油管径mm");
        writer.addHeaderAlias("pipeBackThickness", "回油壁厚mm");
        writer.addHeaderAlias("fluidProduction", "流量t/d");
        writer.addHeaderAlias("waterCut", "含水率%");
        // 一次性写出内容，使用默认样式，强制输出标题
        writer.write(list, true);
        // 设置浏览器响应的格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("海118站计量间数据", "UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out, true);
        out.close();
        writer.close();
    }

    //导入接口
    @PostMapping("/import")
    public Boolean imp(MultipartFile file) throws Exception {
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //List<Station> list = reader.readAll(Station.class);
        //方式2：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Station> stations = CollUtil.newArrayList();
        for (List<Object> row : list) {
            Station station = new Station();
            station.setFacility(row.get(0).toString());
            station.setRoom(row.get(1).toString());
            station.setPipeLength(Double.valueOf(row.get(2).toString()));
            station.setPipeGoDiameter(Double.valueOf(row.get(3).toString()));
            station.setPipeGoThickness(Double.valueOf(row.get(4).toString()));
            station.setPipeBackDiameter(Double.valueOf(row.get(5).toString()));
            station.setPipeBackThickness(Double.valueOf(row.get(6).toString()));
            station.setFluidProduction(Double.valueOf(row.get(7).toString()));
            station.setWaterCut(Double.valueOf(row.get(8).toString()));
            stations.add(station);
        }
        stationService.saveBatch(stations);
        return true;
    }
}

