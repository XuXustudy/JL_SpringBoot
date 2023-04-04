package com.example.springboot.simulate;


import com.example.springboot.entity.*;


import com.example.springboot.utils.IMatrixCal;
import lombok.Data;
import org.apache.commons.math3.linear.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@Data
public class Simulate {

    private double[] temperature;

    private double[] pressure;

    private double[] deltP;

    private double[] Wm;

    private static final Logger logger = LoggerFactory.getLogger(Simulate.class);

    private Network network;

    public Simulate(Network network) {
        this.network = network;
    }


    public void Simulate() {

        //进行管网的初始化;将节点属性值与管道起终点相关联
        network.init();
        List<Node> nodes = network.getNodeList();
        List<Pipe> pipes = network.getPipeList();

//        double sumM = 0;
//        double sumPre = 0;

        int nodeNum = nodes.size();
        int pipeNum = pipes.size();

        for (int i = 0; i < nodeNum; i++) {
            Node node = nodes.get(i);
//            sumPre += node.getPressure();
//            sumM += node.getFlowLoad();      //flowload采用质量流量来进行计算
        }
        //设置管段流量
//        double avePre = sumPre / nodeNum;     //压力，Pa
//        double aveM = sumM / nodeNum;

        double aveM = 5.0;         //设定平均质量流量为  5kg/s

        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.setQuantityM(aveM);

            //如果水力类型中   不包含压力P,代表未知压力节点,对压力进行初始化
//            if (!pipe.getStartNode().getHydraulicType().contains("P")) {
//                pipe.setStartPressure(avePre);
//            }
//            if (!pipe.getEndNode().getHydraulicType().contains("P")) {
//                pipe.setEndPressure(avePre);
//            }
        }

        /**
         *  对管道温度进行赋值
         *  1、对已知温度节点，将温度设置为  流入管道的起点温度
         *  2、对未知温度的节点，将温度设置温  埋地温度
         * */

        for (int i = 0; i < nodeNum; i++) {
            Node node = nodes.get(i);
            if (node.getThermalType().equals("T")) {
                List<Pipe> inPipes = node.getInPipe();

                for (int i1 = 0; i1 < inPipes.size(); i1++) {
                    inPipes.get(i1).setStartTemperature(node.getTemperature());
                }
            } else {
                List<Pipe> inPipes = node.getInPipe();
                List<Pipe> outPipes = node.getOutPipe();
                for (int j = 0; j < inPipes.size(); j++) {
                    Pipe inPipe = inPipes.get(j);
                    inPipe.setStartTemperature(inPipe.getGroundTemperature());
                }
                for (int j = 0; j < outPipes.size(); j++) {
                    Pipe outPipe = outPipes.get(j);
                    outPipe.setEndTemperature(outPipe.getGroundTemperature());

                }

            }
        }

        //进行压力收敛的计算，
        double[] oldPre = new double[nodeNum];
        double[] newPre = new double[nodeNum];

        double[] oldTem = new double[2 * pipeNum];
        double[] newTem = new double[2 * pipeNum];

        double[] Wm = new double[pipeNum];
        double[] yiTa = new double[pipeNum];

        double[] deltP;

        int convergenceP = 0;
        int convergenceT = 0;

        do {

            for (int i = 0; i < 100; i++) {

                setFluidOfPipe(network);         //为管网中的每条管道设置  流体类型

                for (int i1 = 0; i1 < nodeNum; i1++) {
                    oldPre[i1] = network.getNodeList().get(i1).getPressure();
                }

                for (int i1 = 0; i1 < pipeNum; i1++) {
                    Pipe pipe = pipes.get(i1);

                    double yita = 0;
                    /**
                     * 通过压降模型得到   η
                     * */
                    LiquidPipeDPModel liquidPipeDPModel = new LiquidPipeDPModel(pipe);
                    yita = liquidPipeDPModel.getYita();
                    yiTa[i1] = yita;
                }

                deltP = solvePq(network, yiTa);    //只求压力P,判定后再进行 求流量

                network.init();

                for (int i1 = 0; i1 < nodeNum; i1++) {
                    Node node = network.getNodeList().get(i1);
                    newPre[i1] = node.getPressure();
                }

                logger.info("迭代次数为：" + convergenceP + "得到的压力结果为：" + Arrays.toString(newPre));
                convergenceP++;

                if (IMatrixCal.judge(oldPre, newPre, "P")) {
                    break;
                }

                /**
                 * 在不收敛的条件下 设置管道的质量流量
                 * */

                for (int j = 0; j < pipeNum; j++) {
                    Wm[j] = deltP[j] * yiTa[j];
                    logger.info("第" + j + "段管道的质量流量为：" + Wm[j]);
                    pipes.get(j).setQuantityM(Wm[j]);
                }
                this.Wm = Wm;
                this.pressure = newPre;
                this.deltP = deltP;

                logger.error("压降为"+Arrays.toString(deltP));

            }


            // 压力迭代计算结束

            for (int i = 0; i < pipes.size(); i++) {
                Pipe pipe = pipes.get(i);
                oldTem[2 * i] = pipe.getStartTemperature();
                oldTem[2 * i + 1] = pipe.getEndTemperature();
            }


            logger.info("开始计算管道温度");

            network.getSequenceOfT();    //重新产生每个节点的温度求解顺序

            // 计算 管网 温度
            newTem = solveT(network);


            this.temperature = newTem;

            logger.info("温度迭代次数为：" + convergenceT + "管网温度计算中。。。");

            convergenceT++;

        } while (!IMatrixCal.judge(oldTem, newTem, "T"));

    }


    /**
     * 求解节点压力和载荷
     *
     * @param network 对管网进行分块计算
     * @param yiTa    计算得到的导纳向量
     */
    public double[] solvePq(Network network, double[] yiTa) {
        List<Node> nodes = network.getNodeList();
        List<Pipe> oldPipes = network.getPipeList();
        int[][] incidenceMatrix = network.getIncidenceMatrix();

        double[][] A = new double[nodes.size()][oldPipes.size()];
        double[][] yiTaMatrix = new double[oldPipes.size()][oldPipes.size()];
        for (int i = 0; i < yiTaMatrix.length; i++) {
            yiTaMatrix[i][i] = yiTa[i];
        }
        //得到系数矩阵A
        /**
         * yiTa  管道的导纳矩阵
         * A yiTa A转秩  P = w   w—节点的载荷
         * */

        for (int i = 0; i < nodes.size(); i++) {
            for (int i1 = 0; i1 < oldPipes.size(); i1++) {
                for (int j = 0; j < oldPipes.size(); j++) {
                    A[i][i1] += incidenceMatrix[i][j] * yiTaMatrix[j][i1];
                }
            }
        }

        double[][] AMatrix = new double[nodes.size()][nodes.size()];

        for (int i = 0; i < nodes.size(); i++) {
            for (int i1 = 0; i1 < nodes.size(); i1++) {
                for (int j = 0; j < oldPipes.size(); j++) {
                    AMatrix[i][i1] += A[i][j] * incidenceMatrix[i1][j];
                }
            }
        }

        /**
         * 将A矩阵进行分块，对应于不同的节点 已知压力 P、已知流量 Q、未知压力流量 No、已知压力流量 PQ
         * */
        List<Node> nodesP = new ArrayList<>();
        List<Node> nodesQ = new ArrayList<>();
        List<Node> nodesPQ = new ArrayList<>();
        List<Node> nodesNo = new ArrayList<>();


        // 节点已知压力、已知载荷、已知压力和载荷、未知压力和载荷，进行矩阵分块计算
        for (int i = 0; i < nodes.size(); i++) {
            Node node = nodes.get(i);
            String nodeType = nodes.get(i).getHydraulicType();
            //不考虑温度下管网的计算
            if (nodeType.equals("P")) {        //节点指定压力
                nodesP.add(node);    //已知压力P的节点
            } else if (nodeType.equals("Q")) {  //节点指定流量
                nodesQ.add(node);
            } else if (nodeType.equals("PQ")) {  //节点指定压力、流量
                nodesPQ.add(node);
            } else if (nodeType.equals("No")) {  // 节点未指定压力和流量
                nodesNo.add(node);
            }
        }
        //对应于所有类型节点的载荷
        double[] qQ = new double[nodesQ.size()];
        double[] qNo = new double[nodesNo.size()];
        double[] qP = new double[nodesP.size()];
        double[] qPQ = new double[nodesPQ.size()];
        //对应于所有类型节点的压力
        double[] pQ = new double[nodesQ.size()];
        double[] pNo = new double[nodesNo.size()];
        double[] pP = new double[nodesP.size()];
        double[] pPQ = new double[nodesPQ.size()];

        for (int i = 0; i < nodesQ.size(); i++) {
            qQ[i] = -nodesQ.get(i).getFlowLoad();
            pQ[i] = nodesQ.get(i).getPressure();
        }
        for (int i = 0; i < nodesNo.size(); i++) {
            qNo[i] = -nodesNo.get(i).getFlowLoad();
            pNo[i] = nodesNo.get(i).getPressure();
        }
        for (int i = 0; i < nodesP.size(); i++) {
            qP[i] = -nodesP.get(i).getFlowLoad();
            pP[i] = nodesP.get(i).getPressure();
        }
        for (int i = 0; i < nodesPQ.size(); i++) {
            qPQ[i] = -nodesPQ.get(i).getFlowLoad();
            pPQ[i] = nodesPQ.get(i).getPressure();
        }


        double[][] AQ_Q = new double[nodesQ.size()][nodesQ.size()];
        double[][] AQ_N = new double[nodesQ.size()][nodesNo.size()];
        double[][] AQ_P = new double[nodesQ.size()][nodesP.size()];
        double[][] AQ_PQ = new double[nodesQ.size()][nodesPQ.size()];

        double[][] AN_Q = new double[nodesNo.size()][nodesQ.size()];
        double[][] AN_N = new double[nodesNo.size()][nodesNo.size()];
        double[][] AN_P = new double[nodesNo.size()][nodesP.size()];
        double[][] AN_PQ = new double[nodesNo.size()][nodesPQ.size()];

        double[][] APQ_Q = new double[nodesPQ.size()][nodesQ.size()];
        double[][] APQ_N = new double[nodesPQ.size()][nodesNo.size()];
        double[][] APQ_P = new double[nodesPQ.size()][nodesP.size()];
        double[][] APQ_PQ = new double[nodesPQ.size()][nodesPQ.size()];

        double[][] AP_Q = new double[nodesP.size()][nodesQ.size()];
        double[][] AP_N = new double[nodesP.size()][nodesNo.size()];
        double[][] AP_P = new double[nodesP.size()][nodesP.size()];
        double[][] AP_PQ = new double[nodesP.size()][nodesPQ.size()];
        /**
         * nodesP  节点中已知压力P ;  nodesQ  节点中已知节点载荷Q  ;  nodesPQ   节点中已知压力和流量  ；nodesNo  节点中压力和流量都未知
         * */


        double[][][] ABlockQ = IMatrixCal.getABlock(AMatrix, nodesQ, nodesNo, nodesP, nodesPQ);
        AQ_Q = ABlockQ[0];     //已知节点载荷Q 对应于 其压力P1的分块
        AQ_N = ABlockQ[1];
        AQ_P = ABlockQ[2];
        AQ_PQ = ABlockQ[3];

        double[][][] ABlockN = IMatrixCal.getABlock(AMatrix, nodesNo, nodesQ, nodesP, nodesPQ);
        AN_N = ABlockN[0];
        AN_Q = ABlockN[1];
        AN_P = ABlockN[2];
        AN_PQ = ABlockN[3];

        double[][][] ABlockPQ = IMatrixCal.getABlock(AMatrix, nodesPQ, nodesQ, nodesNo, nodesP);
        APQ_PQ = ABlockPQ[0];
        APQ_Q = ABlockPQ[1];
        APQ_N = ABlockPQ[2];
        APQ_P = ABlockPQ[3];

        double[][][] ABlockP = IMatrixCal.getABlock(AMatrix, nodesP, nodesQ, nodesNo, nodesPQ);
        AP_P = ABlockP[0];
        AP_Q = ABlockP[1];
        AP_N = ABlockP[2];
        AP_PQ = ABlockP[3];

        /**
         * 进行已知载荷未知压力节点 压力 P1 、 P2   的求解 , 压力计算方面有问题 ，导纳矩阵求解出错
         * */
        double[][] AQPQ2P = new double[nodesQ.size() + nodesPQ.size()][nodesQ.size() + nodesNo.size()];

        for (int i = 0; i < nodesQ.size() + nodesPQ.size(); i++) {

            for (int j = 0; j < nodesQ.size() + nodesPQ.size(); j++) {
                if (i < nodesQ.size() && j < nodesQ.size()) {
                    AQPQ2P[i][j] = AQ_Q[i][j];
                }
                if (i < nodesQ.size() && j >= nodesQ.size()) {
                    AQPQ2P[i][j] = AQ_N[i][j - nodesQ.size()];
                }

                if (i >= nodesQ.size() && j < nodesQ.size()) {
                    AQPQ2P[i][j] = APQ_Q[i - nodesQ.size()][j];
                }

                if (i >= nodesQ.size() && j >= nodesQ.size()) {
                    AQPQ2P[i][j] = APQ_N[i - nodesQ.size()][j - nodesQ.size()];
                }
            }
        }
        double[] AQ_PMultpP = IMatrixCal.getMultiply(AQ_P, pP);
        double[] AQ_PQMultpPQ = IMatrixCal.getMultiply(AQ_PQ, pPQ);
        double[] APQ_PMultpP = IMatrixCal.getMultiply(APQ_P, pP);
        double[] APQ_PQMultpPQ = IMatrixCal.getMultiply(APQ_PQ, pPQ);

        double[] qConstant1 = new double[qQ.length];
        for (int i = 0; i < qQ.length; i++) {
            qConstant1[i] = qQ[i] - AQ_PMultpP[i] - AQ_PQMultpPQ[i];
        }

        double[] qConstant2 = new double[qPQ.length];
        for (int i = 0; i < qPQ.length; i++) {
            qConstant2[i] = qPQ[i] - APQ_PMultpP[i] - APQ_PQMultpPQ[i];
        }

        double[] qConstant = new double[qQ.length + qPQ.length];
        for (int i = 0; i < qConstant.length; i++) {
            if (i < qQ.length) {
                qConstant[i] = qConstant1[i];
            } else {
                qConstant[i] = qConstant2[i - qQ.length];
            }
        }

        //进行压降 deltP 的计算, A并非满秩矩阵，除去压力已知的那一节点

        RealMatrix coefficient = new Array2DRowRealMatrix(AQPQ2P, false);
        DecompositionSolver solver = new LUDecomposition(coefficient).getSolver();

        RealVector constant = new ArrayRealVector(qConstant);

        RealVector solution = solver.solve(constant);

        for (int i = 0; i < pQ.length; i++) {
            pQ[i] = solution.getEntry(i);
//            double p = nodesQ.get(i).getPressure();
            nodesQ.get(i).setPressure(pQ[i]);

        }
        logger.info("已为已知知载荷   节点进行赋值 压力赋值" + Arrays.toString(pQ));
        for (int i = 0; i < pNo.length; i++) {
            pNo[i] = solution.getEntry(pQ.length + i);
//            double p = nodesNo.get(i).getPressure();
            nodesNo.get(i).setPressure(pNo[i]);
        }

        logger.info("已为未知载荷、未知压力 节点进行赋值 压力赋值" + Arrays.toString(pNo));

        /**
         * 得到未知的载荷 q  并对节点未知载荷赋值
         * */

        double[] calQ_No = IMatrixCal.getAddVector(IMatrixCal.getMultiply(AN_Q, pQ), IMatrixCal.getMultiply(AN_N, pNo),
                IMatrixCal.getMultiply(AN_P, pP), IMatrixCal.getMultiply(AN_PQ, pPQ));
        if (qNo.length == calQ_No.length) {
            qNo = calQ_No;
            for (int i = 0; i < qNo.length; i++) {
                nodesNo.get(i).setFlowLoad(-qNo[i]);
            }
            logger.info("已为未知载荷、未知压力节点  进行【载荷赋值】" + Arrays.toString(qNo));
        }

        double[] calQ_P = IMatrixCal.getAddVector(IMatrixCal.getMultiply(AP_Q, pQ), IMatrixCal.getMultiply(AP_N, pNo),
                IMatrixCal.getMultiply(AP_P, pP), IMatrixCal.getMultiply(AP_PQ, pPQ));
        if (qP.length == calQ_P.length) {
            qP = calQ_P;
            for (int i = 0; i < qP.length; i++) {
                nodesP.get(i).setFlowLoad(-qP[i]);
            }
            logger.info("已为已知压力、载荷的节点  进行【载荷赋值】" + Arrays.toString(qP));
        }

        /**
         * 计算压降向量，deltP
         * */

        double[] deltP = new double[oldPipes.size()];

        for (int i = 0; i < oldPipes.size(); i++) {
            Pipe pipe = oldPipes.get(i);
            deltP[i] = pipe.getStartNode().getPressure() - pipe.getEndNode().getPressure();

        }
        return deltP;
    }

    public double[] solveT(Network network) {

        List<Node> nodes = network.getNodeList();

        boolean calAllT;
        //对于计算得到的管道的移除对象
        List<Pipe> pipeRemove = new ArrayList<>();

        int calNum = 0;

        do {

            calAllT = false;     // 假设所有管段温度都计算完成


            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                List<Integer> sequenceOfT = node.getSequenceOfT();          //深度克隆一个list
                //节点只作为管道的入口;而不作为管道的出口   首先计算所有入口管道的终点温度;

                //判定节点是否为管网的起点
                boolean isNetworkStart = true;
                for (int i1 = 0; i1 < sequenceOfT.size(); i1++) {
                    if (sequenceOfT.get(i1) > 0) {
                        isNetworkStart = false;
                        break;
                    }
                }

                if ((sequenceOfT.size() == 1 && sequenceOfT.get(0) < 0) ||
                        (isNetworkStart && calNum == 0)) {

                    if (node.getOutPipe().size() == 0) {          //节点只流入管道，不流出 ;  一条管道作为流入，考虑多条管道作为流入的情况

                        List<Pipe> inPipes = node.getInPipe();
                        for (int i1 = 0; i1 < inPipes.size(); i1++) {
                            Pipe pipe = node.getInPipe().get(i1);
                            LiquidDTModel liquidDTModel = new LiquidDTModel(pipe);

                            double Tz = liquidDTModel.getTZ();

                            pipe.setEndTemperature(Tz);

                            logger.info(pipe.getPipeName() + "终点温度设置为：" + Tz);
                            pipeRemove.add(pipe);
                        }


                    } else if (node.getOutPipe().size() == 1 && node.getFlowLoad() == 0) {       //有上游来流节点，考虑沿程载荷带来的热量   ，  来流q,来流q的温度 ，必需增加虚拟管道


                        Pipe pipe = node.getOutPipe().get(0);
                        node.setTemperature(pipe.getEndTemperature());     //节点只有一个流出管道


                        List<Pipe> inPipes = node.getInPipe();

                        for (int i1 = 0; i1 < inPipes.size(); i1++) {
                            Pipe inPipe = inPipes.get(i1);

                            inPipe.setStartTemperature(node.getTemperature());
                            logger.info(inPipe.getPipeName() + "入口温度设置为：" + node.getTemperature());

                            LiquidDTModel liquidDTModel = new LiquidDTModel(inPipe);
                            double Tz = liquidDTModel.getTZ();
                            inPipe.setEndTemperature(Tz);
                            pipeRemove.add(inPipe);

                            logger.info(inPipe.getPipeName() + "出口温度设置为：" + Tz);

                        }


                    } else if (node.getOutPipe().size() > 1 || node.getFlowLoad() != 0) {    //有不止一条管道流出向节点

                        List<Pipe> outPipes = node.getOutPipe();
                        List<Pipe> inPipes = node.getInPipe();
                        double Heat = 0;

                        double heatOfNodeLoad = 0;
                        double flowLoad = node.getFlowLoad();

                        if (flowLoad < 0) {
                            heatOfNodeLoad = -calHeatOfNodeLoad(node);
                        }

                        for (int i1 = 0; i1 < outPipes.size(); i1++) {
                            Pipe outPipe = outPipes.get(i1);
                            //如果管道有多个流出，多个流出温度一致
                            double t = outPipe.getEndTemperature();

                            Heat += outPipe.getEndTemperature() * outPipe.getQuantityM() *
                                    outPipe.getFluid().getC(t);    //得到流出管道温度压力总热量
                        }

                        /** 具有流入则加至总热量中*/
                        Heat = Heat + heatOfNodeLoad;

                        double inQuantityM = 0;
                        for (int i1 = 0; i1 < inPipes.size(); i1++) {
                            Pipe inPipe = inPipes.get(i1);
                            inQuantityM += inPipe.getQuantityM();
                        }

                        double outWaterLoad = 0;
                        if (flowLoad > 0) {
                            outWaterLoad = flowLoad;     //向外界流出

                        }

                        double inTemperature = 10;
                        double startTemperature = 0;

                        //出口温度计算有误！！！
                        do {

                            startTemperature = Heat / (inQuantityM * inPipes.get(0).getFluid().getC(inTemperature)
                                    + calHeatOfNodeLoad(outWaterLoad, inTemperature) / inTemperature);

                            inTemperature = (inTemperature + startTemperature) / 2;

                        } while (Math.abs(inTemperature - startTemperature) > 1e-2);

                        // 令节点温度为 流入管道温度
                        for (int i1 = 0; i1 < inPipes.size(); i1++) {
                            logger.info(inPipes.get(i1).getPipeName() + "入口温度设置为：" + inTemperature);
                        }

                        //去除管道中增加
                        for (int i1 = 0; i1 < inPipes.size(); i1++) {
                            Pipe inPipe = inPipes.get(i1);
                            inPipe.setStartTemperature(inTemperature);
                            //设定管道起点温度
                            inPipe.getStartNode().setTemperature(inTemperature);
                            logger.info(inPipe.getStartNode().getNodeName() + "节点温度设置为：" + inTemperature);
                            LiquidDTModel liquidDTModel = new LiquidDTModel(inPipe);
                            double Tz = liquidDTModel.getTZ();
                            inPipe.setEndTemperature(Tz);

                            if (inPipe.getEndNode().getInPipe().size() == 0) {      //如果管道终点没有  再次流入 ，则对管道终节点赋值为  管道终点温度

                                inPipe.getEndNode().setTemperature(Tz);
                                logger.info(inPipe.getEndNode().getNodeName() + "完成温度赋值：" + Tz);

                            }
                            logger.info(inPipe.getPipeName() + "完成出口温度赋值" + Tz);

                            pipeRemove.add(inPipe);
                        }

                    }
                }
            }


            //遍历所有节点，除去已计算的管道
            for (int i = 0; i < nodes.size(); i++) {
                Node node = nodes.get(i);
                List<Integer> sequenceOfT = node.getSequenceOfT();

                boolean isNetworkStart = true;
                for (int i1 = 0; i1 < sequenceOfT.size(); i1++) {
                    if (sequenceOfT.get(i1) > 0) {
                        isNetworkStart = false;
                        break;
                    }
                }

                for (int i1 = 0; i1 < pipeRemove.size(); i1++) {

                    Integer removeInteger = pipeRemove.get(i1).getPipeId();
                    //包含元素则除去已计算的管道Id
                    if (sequenceOfT.size() == 1 && sequenceOfT.contains(-removeInteger) ||
                            (isNetworkStart == true && calNum == 0)) {

                        //remove方法放入的参数为  对象索引
                        if (sequenceOfT.size() != 0) {
                            sequenceOfT.remove(sequenceOfT.indexOf(-removeInteger));
                        }
                    }

                    if (sequenceOfT.contains(removeInteger)) {
                        sequenceOfT.remove(sequenceOfT.indexOf(removeInteger));
                    }
                }

                //当节点列表中仍有元素，则未计算所有管道的温度
                if (sequenceOfT.size() != 0) {
                    calAllT = true;
                }

//                if (pipeRemove.size() == network.getPipeList().size()) {
//                    calAllT = true;
//                }
            }
            calNum++;

        } while (calAllT);

        List<Pipe> pipes = network.getPipeList();
        double[] newTem = new double[2 * pipes.size()];
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            newTem[2 * i] = pipe.getStartTemperature();
            newTem[2 * i + 1] = pipe.getEndTemperature();
        }
        return newTem;
    }

    /**
     * 节点流入油品的热量
     */
    public double calHeatOfNodeLoad(Node node) {

        Fluid2 fluid2 = node.getLoadFluid();

        double temperature = fluid2.getOil().getOilTemperature();

        double C = fluid2.getC(temperature);
        double heat = node.getFlowLoad() * C * temperature;
        return heat;
    }

    //    /**
//     * 节点流出水的热量
//     */
    public double calHeatOfNodeLoad(double flowLoad, double temperature) {
        Water water = new Water();
        double C = water.getC(temperature);
        double heat = flowLoad * C * temperature;
        return heat;
    }


    /**
     * 在计算温度的过程中更新  管道中的流体类型  其更新顺序同温度的求解顺序；得到管道流量，得到后续管道流体的物性参数
     */
    public void setFluidOfPipe(Network network) {
        List<Pipe> pipes = network.getPipeList();
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            Node node = pipe.getStartNode();

            /**
             * 节点的出口管道大小等于0，为管道起点，管道流体为节点流体
             * */

            if (node.getOutPipe().size() == 0) {

                pipe.setFluid(node.getLoadFluid());

            } else if (node.getOutPipe().size() != 0) {

                Fluid2 fluid2 = node.getLoadFluid();
                double flowLoad = node.getFlowLoad();
                /**可能为油，可能为水*/

                double quantityMOfWater = 0;
                double quantityMOfOil = 0;
                double density20 = 0;
                double oilM = 0;

                //流入为油水混合物
                if (flowLoad < 0) {
                    flowLoad = -flowLoad;
                    quantityMOfWater = fluid2.getWaterCut() * flowLoad;
                    quantityMOfOil = flowLoad - quantityMOfWater;
                    density20 = fluid2.getOil().getDensity20();
                    oilM += density20 * quantityMOfOil;
                } else if (flowLoad > 0) {
                    quantityMOfWater = -fluid2.getWaterCut() * flowLoad;
                    quantityMOfOil = -(flowLoad + quantityMOfWater);
                }


                List<Pipe> outPipes = node.getOutPipe();
                for (int i1 = 0; i1 < outPipes.size(); i1++) {
                    Pipe outPipe = outPipes.get(i1);


                    double qMP = Math.abs(outPipe.getQuantityM());

                    double qMW = outPipe.getFluid().getWaterCut() * qMP;

                    quantityMOfWater += qMW;

                    double qMO = qMP - qMW;

                    quantityMOfOil += qMO;

                    if (outPipe.getFluid().getOil() != null) {
                        Oil oil = outPipe.getFluid().getOil();
                        oilM += oil.getDensity20() * qMO;

                    }
                }

                double density20OfPipe = oilM / quantityMOfOil;

                /**对于水量大、油量小，导致 水量为负，油量为正，含水率计算大于  1  ;  对于水量增加绝对值*/

                double waterCutOfPipe = Math.abs(quantityMOfWater) / (quantityMOfOil + Math.abs(quantityMOfWater));
                Oil oilOfPipe = new Oil(density20OfPipe, waterCutOfPipe);
                Fluid2 fluid2OfPipe = new Fluid2(oilOfPipe);
                pipe.setFluid(fluid2OfPipe);
            }

        }

    }
}







