package com.example.springboot.controller.MeteringPlant;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.entity.Station;
import com.example.springboot.service.IStationService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/rang47")
public class Rang47RoomController {

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

    //查询所有数据接口
    @GetMapping
    public List<Station> findAll() {  //mapper中UserMapper的findAll方法，返回值是List<User>
        return stationService.list();
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
        if (!"".equals(facility)){
            queryWrapper.like("facility",facility);
        }
        if (!"".equals(room)){
            queryWrapper.like("room",room);
        }
        queryWrapper.orderByDesc("id");
        return stationService.page(new Page<>(pageNum, pageSize), queryWrapper);
    }

    //计算接口
    @GetMapping("/length")
    public Result length() {
        return Result.success(stationService.selectLength47());
    }
}

