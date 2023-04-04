package com.example.springboot.utils;

import com.example.springboot.entity.Node;

import java.util.List;

public class IMatrixCal {

    /**
     * 实现多个一维数组的加和
     */
    public static double[] getAddVector(double[]... a) {
        int n = a[0].length;

        double[] add = new double[n];
        for (int i = 0; i < a[0].length; i++) {
            if (a[i].length != n) {
                throw new RuntimeException();
            } else {
                for (int i1 = 0; i1 < a.length; i1++) {
                    add[i] += a[i1][i];
                }
            }

        }
        return add;
    }

    public static double[][][] getABlock(double[][] A, List<Node> nodes1, List<Node> nodes2, List<Node> nodes3, List<Node> nodes4) {

        double[][][] A1_4 = new double[4][][];
        double[][] A11 = new double[nodes1.size()][nodes1.size()];
        double[][] A12 = new double[nodes1.size()][nodes2.size()];
        double[][] A13 = new double[nodes1.size()][nodes3.size()];
        double[][] A14 = new double[nodes1.size()][nodes4.size()];

        for (int i = 0; i < nodes1.size(); i++) {  //已知节点载荷节点
            int n = nodes1.get(i).getNodeId() - 1;
            for (int i1 = 0; i1 < nodes1.size(); i1++) {
                int m = nodes1.get(i1).getNodeId() - 1;
                A11[i][i1] = A[n][m];
            }

            for (int i1 = 0; i1 < nodes2.size(); i1++) {
                int m = nodes2.get(i1).getNodeId() - 1;
                A12[i][i1] = A[n][m];
            }

            for (int i1 = 0; i1 < nodes3.size(); i1++) {
                int m = nodes3.get(i1).getNodeId() - 1;
                A13[i][i1] = A[n][m];
            }

            for (int i1 = 0; i1 < nodes4.size(); i1++) {
                int m = nodes4.get(i1).getNodeId() - 1;
                A14[i][i1] = A[n][m];
            }
        }

        A1_4[0] = A11;
        A1_4[1] = A12;
        A1_4[2] = A13;
        A1_4[3] = A14;
        return A1_4;
    }

    public static double[] getMultiply(double[][] Y, double[] p) {
        double[] mult = new double[Y.length];
        for (int i = 0; i < Y.length; i++) {
            for (int i1 = 0; i1 < p.length; i1++) {
                mult[i] += Y[i][i1] * p[i1];
            }
        }
        return mult;
    }

    public static boolean judge(double[] oldPT, double[] newPT, String paramType) {
        boolean convergence = true;
        int n = oldPT.length;
        if (paramType.equals("P")) {
            for (int i = 0; i < n; i++) {
                if (Math.abs(oldPT[i] - newPT[i]) > 1e-3) {
                    convergence = false;
                    break;
                }
            }
        } else if (paramType.equals("T")) {
            for (int i = 0; i < n; i++) {
                if (Math.abs(oldPT[i] - newPT[i]) > 1e-2) {
                    convergence = false;
                    break;
                }
            }
        }
        return convergence;
    }
}
