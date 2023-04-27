package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.INetworktableService;
import com.example.springboot.entity.Networktable;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-04-26
 */
@RestController
@RequestMapping("/networktable")
public class NetworktableController {

    @Resource
    private INetworktableService networktableService;

    //新增或者更新数据接口
    @PostMapping
    public Result save(@RequestBody Networktable networktable){
            return Result.success(networktableService.saveOrUpdate(networktable));
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
            return Result.success(networktableService.removeById(id));
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(networktableService.removeBatchByIds(ids));
    }

    //查询所有数据接口
    @GetMapping
    public Result findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return Result.success(networktableService.list());
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(networktableService.getById(id));
    }

    //分页查询数据接口
    //http://localhost:9090/networktable/page?pageNum=1&pageSize=10&networkName=
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String networkName) {
        QueryWrapper<Networktable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(networkName)) {
            queryWrapper.like("networkName", networkName);
        }

        return Result.success(networktableService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
}

