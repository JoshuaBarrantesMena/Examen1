package utils;

public class ResultEvaluation {
    private final String variable;
    private final String result;

    public ResultEvaluation(String variable, String result) {
        this.variable = variable;
        this.result = result;
    }

    public String getVariable() { 
        return variable; }
    
    public String getResult() {
        return result; }
}
