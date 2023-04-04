package com.example.springboot.controller.Export;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IExportstationService;
import com.example.springboot.entity.Exportstation;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
@RestController
@RequestMapping("/exportstation")
public class ExportstationController {

    @Resource
    private IExportstationService exportstationService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Exportstation exportstation){
            return exportstationService.saveOrUpdate(exportstation);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return exportstationService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return exportstationService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Exportstation> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return exportstationService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Exportstation findOne(@PathVariable Integer id) {
        return exportstationService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Exportstation> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Exportstation> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return exportstationService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

