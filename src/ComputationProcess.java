import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

public class ComputationProcess {

    private final String lineInput;
    private final List<String> variables = new ArrayList<>();
    private final List<List<Double>> valuesArrays = new ArrayList<>();
    private final LinkedHashMap<String, List<Double>> mapVariablesValues = new LinkedHashMap<>();
    private double[][] valuesTuplesT;
    private final List<Double> results = new ArrayList<>();

    public ComputationProcess (String lineInput){
        this.lineInput=lineInput;
    }

    public String evaluate () throws Exception {
        String[] lineTokens = lineInput.split(";");
        String computationKind = getComputationKind(lineTokens[0]);
        String valuesKind = getValuesKind(lineTokens[0]);
        getVariablesValues(lineTokens[1]);
        if(variables.size()!=valuesArrays.size())
            System.out.println("EXCEPTION");
        getValuesTuples(valuesKind);
        Parser parser;
        List<Node> exprParsed = new ArrayList<>();
        for(int i = 2; i<lineTokens.length; i++){
            parser = new Parser(lineTokens[i]);
            exprParsed.add(parser.parse());
        }
        if(!exprParsed.isEmpty()) {
            for (Node expr : exprParsed){
                for(int i = 0; i < valuesTuplesT.length; i++) {
                    double[] arrayTuple = new double[variables.size()];
                    for(int j = 0; j < variables.size(); j++){
                        arrayTuple[j] = valuesTuplesT[i][j];
                    }
                    double result = calculate(expr, arrayTuple);
                    results.add(result);
                }
            }
            switch(computationKind) {
                case "MIN":
                    int indexMin = results.indexOf(Collections.min(results));
                    return results.get(indexMin).toString();
                case "MAX":
                    int indexMax = results.indexOf(Collections.max(results));
                    return results.get(indexMax).toString();
                case "AVG":
                    double sum = 0;
                    for (double n : results)
                        sum += n;
                    return Double.toString(sum / results.size());
                case "COUNT":
                    return Integer.toString(results.size());
            }
        }
        else return "";
        return "";
    }

    private void getValuesTuples(String valuesKind) {
        int n = mapVariablesValues.keySet().size();
        int solutions = 1;

        if(valuesKind.equals("LIST")){
            //inserire condizione che tutti gli array abbiano stessa lunghezza
            int countTotalList = 0;
            int sumListsSize = 0;
            for(List<Double> tupla : mapVariablesValues.values()){
                sumListsSize += tupla.size();
                countTotalList++;
            }
            if(sumListsSize%countTotalList!=0)
                System.out.println("EXCEPTION: Le liste non hanno tutte lunghezza uguale");
            else {
                double[][] valuesTuplesT = new double[sumListsSize / countTotalList][n];
                for(int i = 0; i<(sumListsSize / countTotalList); i++){
                    int j = 0;
                    for (List<Double> tupla : mapVariablesValues.values()) {
                        valuesTuplesT[i][j] = tupla.get(i);
                    }
                }
            }
        }
        else if(valuesKind.equals("GRID")){
            //cartesian product
            for(List<Double> tupla : mapVariablesValues.values())
                solutions *= tupla.size();
            double[][] valuesTuplesT = new double[solutions][n];
            for(int i=0; i<solutions; i++){
                int j=0;
                int k=1;
                for(List<Double> list : mapVariablesValues.values()){
                    valuesTuplesT[i][j] = list.get((i/k)%list.size());
                    k *= list.size();
                }
            }
        }
        else{
            System.out.println("EXCEPTION: WRONG VALUES KIND");
        }
    }

    private String getValuesKind(String lineToken) {
        return lineToken.substring(lineToken.indexOf("_"));
    }

    private String getComputationKind(String lineToken) {
        return lineToken.substring(0,lineToken.indexOf("_"));
    }

    private void getVariablesValues(String variablesValuesFunction) {
        String[] variablesTokens = variablesValuesFunction.split(",");
        for(String variableValues : variablesTokens){
            String variable = variableValues.substring(0,variableValues.indexOf(":"));
            variables.add(variable);
            String valuesFunction = variableValues.substring(variableValues.indexOf(":"));
            String [] values = valuesFunction.split(":");
            double xLower = Double.parseDouble(values[0]);
            double xStep = Double.parseDouble(values[1]);
            double xUpper = Double.parseDouble(values[2]);
            List<Double> valuesArray = new ArrayList<>();
            for(int i = 1; i<(int) ((xUpper-xLower)/xStep);i++)
                valuesArray.add(xLower + i*xStep);
            valuesArrays.add(valuesArray);
        }
    }

    public double calculate (Node expressionParsed, double[] arrayTuple) throws Exception {
        if(expressionParsed instanceof Operator){
            double a1, a2;
            if(expressionParsed.getChildren().get(0) instanceof Operator){
                a1 = calculate(expressionParsed.getChildren().get(0), arrayTuple);
            }
            else if(expressionParsed.getChildren().get(0) instanceof Variable){
                String variable = ((Variable) expressionParsed).getName();
                boolean varFound = false;
                int index;
                for(String nameVar : variables){
                    if(variable.equals(nameVar)) {
                        index = variables.indexOf(nameVar);
                        varFound = true;
                    }
                }
                if(varFound){
                    a1 = arrayTuple[index];
                }
                else{
                    throw new Exception("EXCEPTION: variable not found");
                }
            }
            else{
                a1 = ((Constant) expressionParsed).getValue();
            }
            if(expressionParsed.getChildren().get(1) instanceof Operator){
                a2 = calculate(expressionParsed.getChildren().get(1), arrayTuple);
            }
            else if(expressionParsed.getChildren().get(1) instanceof Variable){
                a2 = 0;
            }
            else{
                a2 = ((Constant) expressionParsed).getValue();
            }
            double[] a = new double[2];
            a[0] = a1;
            a[1] = a2;
            var function = ((Operator) expressionParsed).getType().getFunction();
            return function.apply(a);
        }
        else if(expressionParsed instanceof Variable){

        }
        else{
            return ((Constant) expressionParsed).getValue();
        }
        return 0;
    }
}
