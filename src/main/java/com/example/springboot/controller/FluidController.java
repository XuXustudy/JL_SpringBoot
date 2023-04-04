package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IFluidService;
import com.example.springboot.entity.Fluid;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-03-10
 */
@RestController
@RequestMapping("/fluid")
public class FluidController {

    @Resource
    private IFluidService fluidService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Fluid fluid){
            return fluidService.saveOrUpdate(fluid);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return fluidService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return fluidService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Fluid> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return fluidService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Fluid findOne(@PathVariable Integer id) {
        return fluidService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Fluid> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Fluid> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return fluidService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

