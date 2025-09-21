package com.mycompany.java.model;

public class OutliersRule implements QualityRule {
    private final  int maxAllowed;

    public OutliersRule(int maxAllowed) {
        this.maxAllowed = maxAllowed;
    }

    

    public String  verify( ColumnMetric  metric) {
        if (metric.getOutliers()> maxAllowed) {
            return "El numero de valores atipicos("+ metric.getOutliers()+") se soprepasa del maximo de"+ maxAllowed ;
        }
        return "OK";
    }
}
