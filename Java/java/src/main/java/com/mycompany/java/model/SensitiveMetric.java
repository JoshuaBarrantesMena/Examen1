import com.mycompany.java.model.ColumnMetric;
import com.mycompany.java.model.QualityRule;
import java.util.List;

public class SensitiveMetric extends ColumnMetric {
    public SensitiveMetric(String variable, double mean, double median, double stddev, int outliers) {
        super(variable, mean, median, stddev, outliers);
    }

    @Override
    public String qualityEvaluate(List<QualityRule> rules) {
        // Para una métrica sensitiva, somos más estrictos. Con que falle una regla, se rechaza.
        for (QualityRule rule : rules) {
            String feedback = rule.verify(this);
            if (!feedback.equals("ok")) {
                return "rechazado (Sensitiva): " + feedback;
            }
        }
        return "Aprovado (Sensitiva)";
    }
}