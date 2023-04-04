package com.example.springboot.controller.TransferStation;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.springboot.entity.Hai118;
import com.example.springboot.service.impl.Hai118ServiceImpl;
import com.example.springboot.service.impl.StationServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author xuyihao
 * @date 2023-02-28
 * @apiNote
 */
@RestController
@RequestMapping("/hai118sta")
public class Hai118StationController {

    @Resource
    private Hai118ServiceImpl hai118Service;

    //新增或者更新数据接口
    @PostMapping
    public boolean save(@RequestBody Hai118 hai118){
        return hai118Service.saveOrUpdate(hai118);
    }

    //删除数据接口
    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Integer id){
        return hai118Service.removeById(id);
    }

    //批量删除数据接口
    @PostMapping("/del/batch")
    public boolean deleteBatch(@RequestBody List<Integer> ids){
        return hai118Service.removeBatchByIds(ids);
    }

    //所有数据查询接口
    @GetMapping()
    public List<Hai118> findAll() {
        return hai118Service.list();
    }

    //根据id查询数据接口
    @GetMapping("/{id}")
    public Hai118 findOne(@PathVariable Integer id) {
        return hai118Service.getById(id);
    }

    //分页查询数据接口
    @GetMapping("/page")
    public Page<Hai118> findPage(@RequestParam Integer pageNum,
                                 @RequestParam Integer pageSize
                                 //@RequestParam(defaultValue = "") String facility
                                 ) {
        QueryWrapper<Hai118> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        return hai118Service.page(new Page<>(pageNum, pageSize),queryWrapper);
    }
}
