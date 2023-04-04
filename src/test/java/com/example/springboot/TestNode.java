package com.example.springboot;

import com.example.springboot.entity.Node;
import com.example.springboot.mapper.NodeMapper;
import com.example.springboot.service.impl.NodeServiceImpl;
import com.example.springboot.service.impl.PipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestNode {
    @Resource
    private NodeServiceImpl nodeImpl;

    @Resource
    private PipeServiceImpl pipeImpl;

    @Resource
    private NodeMapper nodeMapper;

    @Test
    public void find() {
        System.out.println(nodeImpl.getById(1).toString());
    }

    @Test
    public void NewNetwork() {
        List<Node> nodes = nodeMapper.selectList(null);
        System.out.println(nodes.get(1).getNodeName());
    }
}
