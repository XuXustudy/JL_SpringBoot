package com.example.springboot.entity;

import com.example.springboot.mapper.NodeMapper;
import com.example.springboot.mapper.OilMapper;
import com.example.springboot.mapper.PipeMapper;
import com.example.springboot.simulate.EnergyConsumption;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Data
public class Network {
    private List<Node> nodeList;
    private List<Pipe> pipeList;

    @JsonIgnore
    private int[][] incidenceMatrix;


    private EnergyConsumption energyConsumption;

    private List<ArrayList<Double>> energyLossMatrix;


    public Network(List<Node> nodeList, List<Pipe> pipeList) {
        this.nodeList = nodeList;
        this.pipeList = pipeList;

        setPAndNName();

        ConnectPipeAndNode();
        GenerateIncidenceMatrix();
        getSequenceOfT();
    }

    public void init() {
        List<Pipe> pipes = this.getPipeList();

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            Node startNode = pipe.getStartNode();
            Node endNode = pipe.getEndNode();
            pipe.setStartPressure(startNode.getPressure());
            pipe.setEndPressure(endNode.getPressure());
        }
    }

    public void setPAndNName() {
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            node.setNodeName("Node" + node.getNodeId());
        }
        for (int i = 0; i < pipeList.size(); i++) {
            Pipe pipe = pipeList.get(i);
            pipe.setPipeName("Pipe" + pipe.getPipeId());

        }
    }

    public void GenerateIncidenceMatrix() {
        int nodeCount = nodeList.size();
        int pipeCount = pipeList.size();
        int[][] incidenceMatrix = new int[nodeCount][pipeCount];

        for (int i = 0; i < nodeCount; i++) {
            Node node = nodeList.get(i);
            List<Pipe> inPipes = node.getInPipe();       //节点作为管道起点
            List<Pipe> outPipes = node.getOutPipe();     //节点作为管道终点
            //得到所有与节点相关联的元件
            if (inPipes != null) {
                for (int j = 0; j < inPipes.size(); j++) {
                    Pipe pipe = inPipes.get(j);
                    incidenceMatrix[i][pipe.getPipeId() - 1] = 1;    //节点作为管道起点   leaving
                }
            }
            if (outPipes != null) {
                for (int j = 0; j < outPipes.size(); j++) {
                    Pipe pipe = outPipes.get(j);                 //节点作为管道终点   coming
                    incidenceMatrix[i][pipe.getPipeId() - 1] = -1;
                }
            }
        }
        this.incidenceMatrix = incidenceMatrix;
    }

    /* 将节点与原件相连，增加节点中原件属性值——代表与节点相连的所有原件；增加原件中的节点属性*/
    public void ConnectPipeAndNode() {
        for (int i = 0; i < pipeList.size(); i++) {
            Pipe pipe = pipeList.get(i);
            int startNumb = pipe.getStartNumb();
            Node startNode = null;
            for (Node node : nodeList) {
                int numb = node.getNodeId();
                if (numb == startNumb) {
                    startNode = node;
                    break;
                }
            }
            startNode.addPipe(pipe);
            startNode.addInPipe(pipe);         //节点作为元件的起始节点，当作原件的入口
            pipe.setStartNode(startNode);


            int endNumb = pipe.getEndNumb();
            Node endNode = null;
            for (Node node : nodeList) {
                int numb = node.getNodeId();
                if (numb == endNumb) {
                    endNode = node;
                    break;
                }
            }
            endNode.addPipe(pipe);             //一个原件具有两个节点（起始节点对象、终止节点对象）；一个节点具有几个原件对象
            // （包含inElement,OutElement,in表示节点作为元件的起点，out表示节点作为原件的终点）
            endNode.addOutPipe(pipe);          //节点作为原件终止节点，作为原件的出口
            pipe.setEndNode(endNode);              //作为原件的终止节点
        }
    }

    //得到求解温度的顺序
    public void getSequenceOfT() {
        for (int i = 0; i < nodeList.size(); i++) {
            Node node = nodeList.get(i);
            List<Integer> sequenceTNode = new ArrayList<>();
            for (int i1 = 0; i1 < node.getInPipe().size(); i1++) {
                int seq = node.getInPipe().get(i1).getPipeId() * (-1);
                sequenceTNode.add(seq);
            }
            for (int i1 = 0; i1 < node.getOutPipe().size(); i1++) {
                int seq = node.getOutPipe().get(i1).getPipeId();
                sequenceTNode.add(seq);
            }
            node.setSequenceOfT(sequenceTNode);
        }
    }


}
