package utils;

import java.util.List;
import java.util.Map;

public class Configuration {
    public List<RulConfig> rules;
    public Map<String, String> map_variables;

    public static class RulConfig {
        public String type;
        public double parameter;
    }
}
