package com.example.springboot.service.impl;

import com.example.springboot.entity.Networknodetable;
import com.example.springboot.mapper.NetworknodetableMapper;
import com.example.springboot.service.INetworknodetableService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author xyh
 * @since 2023-04-26
 */
@Service
public class NetworknodetableServiceImpl extends ServiceImpl<NetworknodetableMapper, Networknodetable> implements INetworknodetableService {

    @Resource
    private NetworknodetableMapper networknodetableMapper;

    @Override
    public int selectNodeCounts() {
        return networknodetableMapper.Counts();
    }

    @Override
    public List<Object[]> selectNodeLocation() {
        List<Networknodetable> list = networknodetableMapper.selectList(null);
        List<Object[]> resultList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Object[] array = new Object[2];
            array[0] = list.get(i).getLat(); //经度
            array[1] = list.get(i).getLng(); //纬度
            resultList.add(array);
        }
        return resultList;
    }

    @Override
    public List<Map<String, Object>> queryDataByCondition(String condition) {
        List<Map<String, Object>> resultList = networknodetableMapper.queryDataByCondition(condition);
        return resultList;
    }
}
