package com.mycompany.java.model;



public class StandardDeviationRule implements QualityRule {
    private final double maxAllowed;

    public StandardDeviationRule(double maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    @Override
    public String verify(ColumnMetric metric) {
        if (metric.getStddev() > maxAllowed) {
            return "Desviacion estandar (" + metric.getStddev() + ") excede el maximo de " + maxAllowed + ".";
        }
        return "OK";
    }
}