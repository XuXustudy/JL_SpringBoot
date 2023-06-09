package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IOilService;
import com.example.springboot.entity.Oil;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-03-30
 */
@RestController
@RequestMapping("/oil")
public class OilController {

    @Resource
    private IOilService oilService;

    //新增或者更新数据接口
    @PostMapping
    public Result save(@RequestBody Oil oil){
            return Result.success(oilService.saveOrUpdate(oil));
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
            return Result.success(oilService.removeById(id));
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(oilService.removeBatchByIds(ids));
    }

    //查询所有数据接口
    @GetMapping
    public Result findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return Result.success(oilService.list());
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(oilService.getById(id));
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize,
                           @RequestParam(defaultValue = "") String oilName) {
        QueryWrapper<Oil> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        if (!"".equals(oilName)) {
            queryWrapper.like("networkName", oilName);
        }

        return Result.success(oilService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }
}

