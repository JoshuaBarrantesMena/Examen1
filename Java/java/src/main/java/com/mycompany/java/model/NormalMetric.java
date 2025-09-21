package com.mycompany.java.model;

import java.util.List;

public class NormalMetric extends ColumnMetric {

    public NormalMetric(String variable, double mean, double median, double stddev, int outliers) {
        super(variable, mean, median, stddev, outliers);
    }


    
    // Asegúrate de que este código esté en tu clase MetricaNormal (o su equivalente)

@Override
public String qualityEvaluate(List<QualityRule> rules) {
    // 1. Usamos un StringBuilder SOLO para los mensajes de error.
    StringBuilder errorMsg = new StringBuilder();
    
    // 2. Usamos una bandera para saber si todo está bien. Empezamos asumiendo que sí.
    boolean aproachRules = true;

    // 3. Recorremos cada regla
    for (QualityRule rule : rules) {
        String feedback = rule.verify(this);

        if (!feedback.equals("OK")) {
            aproachRules = false; 
            errorMsg.append(feedback).append(" "); 
        }
    }

    // 5. Al final, decidimos qué devolver basándonos en la bandera.
    if (aproachRules) {
        return "APROBADO";
    } else {
        return "RECHAZADO: " + errorMsg.toString().trim();
    }
}
}