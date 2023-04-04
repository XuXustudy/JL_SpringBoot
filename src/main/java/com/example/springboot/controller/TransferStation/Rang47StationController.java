package com.example.springboot.controller.TransferStation;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IRang47Service;
import com.example.springboot.entity.Rang47;

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
@RequestMapping("/rang47sta")
public class Rang47StationController {

    @Resource
    private IRang47Service rang47Service;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Rang47 rang47){
            return rang47Service.saveOrUpdate(rang47);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return rang47Service.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return rang47Service.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Rang47> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return rang47Service.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Rang47 findOne(@PathVariable Integer id) {
        return rang47Service.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Rang47> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Rang47> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return rang47Service.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

