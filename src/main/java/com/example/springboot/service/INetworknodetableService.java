package com.example.springboot.service;

import com.example.springboot.entity.Networknodetable;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author xyh
 * @since 2023-04-26
 */
public interface INetworknodetableService extends IService<Networknodetable> {

    int selectNodeCounts();

    List<Object[]> selectNodeLocation();

    List<Map<String, Object>> queryDataByCondition(String condition);
}
