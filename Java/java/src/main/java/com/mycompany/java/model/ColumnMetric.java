package com.mycompany.java.model;

import java.util.List;

public abstract class ColumnMetric {

    protected String variable;

    protected double mean;

    protected double median;

    protected double stddev;

    protected int outliers;

    public ColumnMetric(String variable, double mean, double median, double stddev, int outliers) {
        this.variable = variable;
        this.mean = mean;
        this.median = median;
        this.stddev = stddev;
        this.outliers = outliers;
    }
   
    public ColumnMetric() {
    }

    public String getVariable() {
        return variable;
    }

    public double getStddev() {
        return stddev;
    }

    public int getOutliers() {
        return outliers;
    }

    public abstract String qualityEvaluate(List<QualityRule> rules);
}
