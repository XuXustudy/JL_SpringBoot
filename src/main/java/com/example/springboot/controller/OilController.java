package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
    public boolean save(@RequestBody Oil oil){
            return oilService.saveOrUpdate(oil);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return oilService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return oilService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Oil> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return oilService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Oil findOne(@PathVariable Integer id) {
        return oilService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Oil> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Oil> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return oilService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

