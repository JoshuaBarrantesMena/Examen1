package com.mycompany.java.model;

import io.ReadData;
import utils.*;
import com.mycompany.java.model.*;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReportProcessor {

    private final List<ColumnMetric> metrics;
    private final List<QualityRule> rules;

    public ReportProcessor(String rutaStatsCsv, String rutaRulesJson) throws IOException {
        Configuration config = ReadData.readConfigurationJson(rutaRulesJson);

        this.metrics = ReadData.readStatsCsv(rutaStatsCsv, config.map_variables);

        this.rules= new ArrayList<>();
        for (Configuration.RulConfig rc : config.rules) {
            if ("outliers".equalsIgnoreCase(rc.type)) {
                this.rules.add(new OutliersRule((int) rc.parameter));
            } else if ("stddev".equalsIgnoreCase(rc.type)) {
                this.rules.add(new StandardDeviationRule(rc.parameter));
            }
        }
    }

    public List<ResultEvaluation> evaluteMetrics() {
        List<ResultEvaluation> resultados = new ArrayList<>();
        for (ColumnMetric metric : metrics) {
            String resultado = metric.qualityEvaluate(this.rules);
            resultados.add(new ResultEvaluation(metric.getVariable(), resultado));
        }
        return resultados;
    }

    public void generarReportes(List<ResultEvaluation> resultados, String rutaTxt, String rutaCsv) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaTxt))) {
            writer.write(" Reporte Detallado de Calidad de Datos \n\n");
            for (ResultEvaluation res : resultados) {
                writer.write("vareriable: " + res.getVariable() + "\n");
                writer.write("  Resultado: " + res.getResult() + "\n\n");
            }
            writer.write("Fin del Reporte ");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(rutaCsv))) {
            writer.write("variable,estado\n");
            for (ResultEvaluation res : resultados) {
                String estado = res.getResult().startsWith("APROBADO") ? "APROBADO" : "RECHAZADO";//sino
                writer.write(res.getVariable() + "," + estado + "\n");
            }
        }
    }
}