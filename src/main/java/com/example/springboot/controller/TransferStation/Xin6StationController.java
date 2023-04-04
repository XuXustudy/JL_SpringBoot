package com.example.springboot.controller.TransferStation;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IXin6Service;
import com.example.springboot.entity.Xin6;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author xyh
 * @since 2023-02-28
 */
@RestController
@RequestMapping("/xin6sta")
public class Xin6StationController {

    @Resource
    private IXin6Service xin6Service;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Xin6 xin6){
            return xin6Service.saveOrUpdate(xin6);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return xin6Service.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return xin6Service.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Xin6> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return xin6Service.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Xin6 findOne(@PathVariable Integer id) {
        return xin6Service.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Xin6> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Xin6> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return xin6Service.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

