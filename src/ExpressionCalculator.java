import ExpressionParser.Constant;
import ExpressionParser.Node;
import ExpressionParser.Operator;
import ExpressionParser.Variable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ExpressionCalculator {

    private final double[] arrayTuple;
    private final LinkedHashMap<String, List<Double>> mapVariablesValues;

    public ExpressionCalculator(double[] arrayTuple, LinkedHashMap<String, List<Double>> mapVariablesValues){
        this.arrayTuple = arrayTuple;
        this.mapVariablesValues = mapVariablesValues;
    }

    public double calculate(Node expressionParsed) throws Exception {
        if (expressionParsed instanceof Operator) {
            double a1, a2;
            if (expressionParsed.getChildren().get(0) instanceof Operator) {
                a1 = calculate(expressionParsed.getChildren().get(0));
            } else if (expressionParsed.getChildren().get(0) instanceof Variable) {
                String variable = ((Variable) expressionParsed.getChildren().get(0)).getName();
                int index = findIndexVariable(variable, mapVariablesValues);
                if (index!=-1) {
                    a1 = arrayTuple[index];
                } else {
                    throw new Exception("EXCEPTION: variable not found");
                }
            } else {
                a1 = ((Constant) expressionParsed.getChildren().get(0)).getValue();
            }
            if (expressionParsed.getChildren().get(1) instanceof Operator) {
                a2 = calculate(expressionParsed.getChildren().get(1));
            } else if (expressionParsed.getChildren().get(1) instanceof Variable) {
                String variable = ((Variable) expressionParsed.getChildren().get(1)).getName();
                int index = findIndexVariable(variable, mapVariablesValues);
                if (index != -1) {
                    a2 = arrayTuple[index];
                } else {
                    throw new Exception("EXCEPTION: variable not found");
                }
            } else {
                a2 = ((Constant) expressionParsed.getChildren().get(1)).getValue();;
            }
            double[] a = new double[2];
            a[0] = a1;
            a[1] = a2;
            var function = ((Operator) expressionParsed).getType().getFunction();
            return function.apply(a);
        } else if (expressionParsed instanceof Variable) {
            String variable = ((Variable) expressionParsed).getName();
            int index = findIndexVariable(variable, mapVariablesValues);
            if (index != -1) {
                return arrayTuple[index];
            } else {
                throw new Exception("EXCEPTION: variable not found");
            }
        } else {
            return ((Constant) expressionParsed).getValue();
        }
    }

    private static int findIndexVariable (String variable, LinkedHashMap<String, List<Double>> mapVariablesValues){
        List<String> indexes = new ArrayList<>(mapVariablesValues.keySet());
        return indexes.indexOf(variable);
    }
}
