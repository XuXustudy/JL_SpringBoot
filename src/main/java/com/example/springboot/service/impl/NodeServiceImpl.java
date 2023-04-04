package com.example.springboot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.springboot.entity.Node;
import com.example.springboot.mapper.NodeMapper;
import com.example.springboot.service.IHai118Service;
import com.example.springboot.service.INodeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class NodeServiceImpl extends ServiceImpl<NodeMapper, Node> implements INodeService {

}
