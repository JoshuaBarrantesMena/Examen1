package com.mycompany.java.model;
import java.util.List;

public class SensitiveMetric extends ColumnMetric {
    public SensitiveMetric(String variable, double mean, double median, double stddev, int outliers) {
        super(variable, mean, median, stddev, outliers);
    }   
    @Override
    public String qualityEvaluate(List<QualityRule> rules) {
        for (QualityRule rule : rules) {
            String feedback = rule.verify(this);
            if (!feedback.equals("OK")) {
                return "Rechazado (Sensitiva): " + feedback;
            }
        }
        return "Aprovado (Sensitiva)";
    }
}