package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.ICalresultService;
import com.example.springboot.entity.Calresult;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-04-06
 */
@RestController
@RequestMapping("/calresult")
public class CalresultController {

    @Resource
    private ICalresultService calresultService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Calresult calresult){
            return calresultService.saveOrUpdate(calresult);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return calresultService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return calresultService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Calresult> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return calresultService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Calresult findOne(@PathVariable Integer id) {
        return calresultService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Calresult> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Calresult> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return calresultService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

