package com.example.springboot.service;

import com.example.springboot.entity.Exportstation;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyh
 * @since 2023-03-07
 */
public interface IExportstationService extends IService<Exportstation> {

    double CalExportK118();

    double CalExportD118();

    double CalExportK6();

    double CalExportD6();

    double CalExportK47();

    double CalExportD47();

    double calExportEfficiency118();

    double calExportEfficiency6();

    double calExportEfficiency47();
}
