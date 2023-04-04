package com.example.springboot.entity;

public interface FluidPar {

    double getDensity(double temperature);

    double getViscosity(double temperature);

    double getC(double temperature);

    double[] getViscosityPar(double[][] temAndVis);

    double[] getCPar(double[][] temAndC);
}
