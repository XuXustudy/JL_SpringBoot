package com.example.springboot.controller.Export;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.example.springboot.service.IExportfluidService;
import com.example.springboot.entity.Exportfluid;

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
@RequestMapping("/exportfluid")
public class ExportfluidController {

    @Resource
    private IExportfluidService exportfluidService;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Exportfluid exportfluid){
            return exportfluidService.saveOrUpdate(exportfluid);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
            return exportfluidService.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return exportfluidService.removeBatchByIds(ids);
    }

    //查询所有数据接口
    @GetMapping
    public List<Exportfluid> findAll(){  //mapper中UserMapper的findAll方法，返回值是List<User>
        return exportfluidService.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Exportfluid findOne(@PathVariable Integer id) {
        return exportfluidService.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Exportfluid> findPage(@RequestParam Integer pageNum,
                                    @RequestParam Integer pageSize) {
        QueryWrapper<Exportfluid> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return exportfluidService.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}

