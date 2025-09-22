/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.java;
import java.io.IOException;
import java.util.List;
import utils.ResultEvaluation;
import com.mycompany.java.model.ReportProcessor;
/**
 *
 * @author Jose Francisco Rodriguez Arias
 */
public class Main {

   public static void main(String[] args) {
     
        if (args.length < 4) {
            System.err.println("Uso: java -jar <jar_file> <stats.csv> <rules.json> <reporte.txt> <reporte_resumen.csv>");
            System.exit(1);
        }

        String rutaStatsCsv = args[0];
        String rutaRulesJson = args[1];
        String reportTxtPath = args[2]; 
        String reportCsvPath = args[3]; 

        try {
            ReportProcessor processor = new ReportProcessor(rutaStatsCsv, rutaRulesJson);

            List<ResultEvaluation> results = processor.evaluteMetrics();

            processor.generateReports(results, reportTxtPath, reportCsvPath);

            System.out.println("Procesamiento Java completado. Reportes generados en:");
            System.out.println("- " + reportTxtPath);
            System.out.println("- " + reportCsvPath);

        } catch (IOException e) {
            System.err.println("error no se pudo procesar los archivos.");
            e.printStackTrace();
        }
    }
}
