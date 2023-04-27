package com.example.springboot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.common.Result;
import com.example.springboot.mapper.NetworknodetableMapper;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.INetworknodetableService;
import com.example.springboot.entity.Networknodetable;

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
@RequestMapping("/networknodetable")
public class NetworknodetableController {

    @Resource
    private INetworknodetableService networknodetableService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Networknodetable networknodetable){
            return networknodetableService.saveOrUpdate(networknodetable);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return networknodetableService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return networknodetableService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Networknodetable> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return networknodetableService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Networknodetable findOne(@PathVariable Integer id) {
        return networknodetableService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize) {
        QueryWrapper<Networknodetable> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");

        return Result.success(networknodetableService.page(new Page<>(pageNum, pageSize),queryWrapper));
    }

    //查询管网节点表中对应该管网的节点数
    @GetMapping("/getnodecounts")
    public Result NodeCounts() {
        return Result.success(networknodetableService.selectNodeCounts());
    }

    //查询管网节点表中所有的节点坐标
    @GetMapping("/getallnodelocations")
    public Result NodeLocation() {
        return Result.success(networknodetableService.selectNodeLocation());
    }

    //按条件查询管网节点表中对应的该管网的节点坐标
    @GetMapping("/getnodelocations")
    //http://localhost:9090/networknodetable/getnodelocations?condition=
    public List<Map<String, Object>> queryData(@RequestParam(defaultValue = "") String condition) {
        List<Map<String, Object>> resultList = networknodetableService.queryDataByCondition(condition);
        return resultList;
    }
}

