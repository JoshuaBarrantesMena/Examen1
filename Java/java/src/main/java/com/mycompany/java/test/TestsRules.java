package com.mycompany.java.test;

import com.mycompany.java.model.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TestsRules {

    @Test
    @DisplayName("1. OutlierRule should fail if outliers exceed the limit")
    void outlierRuleFailsWhenLimitExceeded() {
        OutliersRule rule = new OutliersRule(5);
        NormalMetric metric = new NormalMetric("test_var", 10, 10, 5, 10); 

        String result = rule.verify(metric);

        assertTrue(result.contains("excede el maximo"));
    }

    @Test
    @DisplayName("2. OutlierRule should pass if outliers are within the limit")
    void outlierRulePassesWhenWithinLimit() {
        OutliersRule rule = new OutliersRule(5);
        NormalMetric metric = new NormalMetric("test_var", 10, 10, 5, 3); 
        String result = rule.verify(metric);
        assertEquals("okey", result);
    }

    @Test
    @DisplayName("3. SensitiveMetric evaluation should be strict and fail fast")
    void sensitiveMetricEvaluationFailsFast() {
        SensitiveMetric metric = new SensitiveMetric("var_sensible", 50, 50, 25, 6);
        List<QualityRule> rules = new ArrayList<>();
        rules.add(new StandardDeviationRule(20.0)); 
        rules.add(new OutliersRule(10)); 

        String result = metric.qualityEvaluate(rules);

        assertTrue(result.startsWith("RECHAZADO"));
        assertTrue(result.contains("Desviacion estandar"));
    }
}
