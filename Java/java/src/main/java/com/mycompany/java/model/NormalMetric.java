package com.mycompany.java.model;

import java.util.List;

public class NormalMetric extends ColumnMetric {

    public NormalMetric(String variable, double mean, double median, double stddev, int outliers) {
        super(variable, mean, median, stddev, outliers);
    }

    @Override
    
    public String qualityEvaluate(List<QualityRule> rules) {
        StringBuilder result = new StringBuilder();
        boolean  satisfy= true;
        for (QualityRule rule : rules) {
            String feedback = rule.verify(this);
            if (!feedback.equals("okeys")) {
                result.append(feedback).append(" ");
                satisfy = false;
            }
        }
        
                       return satisfy ? "Aprovado" : "Rechazado: " + result.toString().trim();
    }
}