package io;

import com.google.gson.Gson;
import com.mycompany.java.model.*;
import utils.Configuration;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReadData {

    public static List<ColumnMetric> readStatsCsv(String rutaCsv, Map<String, String> mapVariables) throws IOException {
        List<ColumnMetric> metrics = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(rutaCsv))) {
            String linea;
            br.readLine(); 

            while ((linea = br.readLine()) != null) {
                String lineaCorregida = linea.replace(',', '.');
                String[] datos = lineaCorregida.split(";");
                if (datos.length < 5) continue; 

                String variable = datos[0].trim();
                double mean = Double.parseDouble(datos[1].trim());
                double median = Double.parseDouble(datos[2].trim());
                double stddev = Double.parseDouble(datos[3].trim());
                int outliers = Integer.parseInt(datos[4].trim());

                String type = mapVariables.getOrDefault(variable, "normal");
                if (type.equalsIgnoreCase("sensitiva")) {
                    metrics.add(new SensitiveMetric(variable, mean, median, stddev, outliers));
                } else {
                    metrics.add(new NormalMetric(variable, mean, median, stddev, outliers));
                }
            }
        }
        return metrics;
    }

    public static Configuration readConfigurationJson(String rutaJson) throws IOException {
        try (FileReader reader = new FileReader(rutaJson)) {
            return new Gson().fromJson(reader, Configuration.class);
        }
    }
}
