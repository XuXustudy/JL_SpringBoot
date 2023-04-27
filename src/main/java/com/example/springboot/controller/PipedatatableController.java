package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IPipedatatableService;
import com.example.springboot.entity.Pipedatatable;

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
@RequestMapping("/pipedatatable")
public class PipedatatableController {

    @Resource
    private IPipedatatableService pipedatatableService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Pipedatatable pipedatatable){
            return pipedatatableService.saveOrUpdate(pipedatatable);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return pipedatatableService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return pipedatatableService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Pipedatatable> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return pipedatatableService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Pipedatatable findOne(@PathVariable Integer id) {
        return pipedatatableService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Pipedatatable> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Pipedatatable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return pipedatatableService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

