package units.progadv.process.computation;

import units.progadv.exceptions.LengthListException;
import units.progadv.process.computation.ExpressionParser.Node;
import units.progadv.process.computation.ExpressionParser.Parser;

import java.util.*;

public class ComputationProcess {

    private final String lineInput;

    public ComputationProcess(String lineInput) {
        this.lineInput = lineInput;
    }

    private static String[] computationErrorResponse(String errorDescription) {
        return new String[]{"true", errorDescription};
    }

    private static String[] computationResultResponse(String result) {
        return new String[]{"false", result};
    }

    public String[] compute() throws Exception {
        String[] lineTokens = requestParser(lineInput);
        String computationKind = lineTokens[0].substring(0, lineTokens[0].indexOf("_"));
        if (!ComputationKind.match(computationKind))
            return computationErrorResponse("Wrong ComputationKind");
        String valuesKind = lineTokens[0].substring(lineTokens[0].indexOf("_") + 1);
        if (!ValuesKind.match(valuesKind))
            return computationErrorResponse("Wrong ValuesKind");
        LinkedHashMap<String, List<Double>> mapVariablesValues = getVariablesValues(lineTokens[1]);
        try {
            double[][] valuesTuplesT = getValuesTuples(valuesKind, mapVariablesValues);
            Parser parser;
            List<Node> exprParsed = new ArrayList<>();
            try {
                for (int i = 2; i < lineTokens.length; i++) {
                    parser = new Parser(lineTokens[i]);
                    exprParsed.add(parser.parse());
                }
            } catch (IllegalArgumentException e) {
                return computationErrorResponse(e.toString());
            }
            if (!exprParsed.isEmpty()) {
                List<Double> results = new ArrayList<>();
                ExpressionCalculator calculator;
                for (Node expr : exprParsed) {
                    for (double[] doubles : valuesTuplesT) {
                        double[] arrayTuple = new double[mapVariablesValues.keySet().size()];
                        System.arraycopy(doubles, 0, arrayTuple, 0, mapVariablesValues.keySet().size());
                        calculator = new ExpressionCalculator(arrayTuple, mapVariablesValues);
                        double result = calculator.calculate(expr);
                        results.add(result);
                    }
                }
                switch (computationKind) {
                    case "MIN":
                        int indexMin = results.indexOf(Collections.min(results));
                        return computationResultResponse(results.get(indexMin).toString());
                    case "MAX":
                        int indexMax = results.indexOf(Collections.max(results));
                        return computationResultResponse(results.get(indexMax).toString());
                    case "AVG":
                        double sumTot = 0;
                        for (double n : results)
                            sumTot += n;
                        return computationResultResponse(Double.toString(sumTot / results.size()));
                    case "COUNT":
                        return computationResultResponse(Integer.toString(results.size()));
                }
            }
        }catch (Exception e){
            return computationErrorResponse(e.getMessage());
        }
        return computationErrorResponse("Empty expression");
    }

    private static String[] requestParser(String lineInput) throws Exception {
        String[] lineTokens;
        lineTokens = lineInput.split(";");
        if (lineTokens.length >= 3)
            return lineTokens;
        else
            throw new Exception("Error in lineInput");
    }

    private LinkedHashMap<String, List<Double>> getVariablesValues(String variablesValuesFunction) {
        LinkedHashMap<String, List<Double>> mapVariablesValues = new LinkedHashMap<>();
        String[] variablesTokens = variablesValuesFunction.split(",");
        for (String variableValues : variablesTokens) {
            String variable = variableValues.substring(0, variableValues.indexOf(":"));
            String valuesFunction = variableValues.substring(variableValues.indexOf(":") + 1);
            String[] values = valuesFunction.split(":");
            double xLower = Double.parseDouble(values[0]);
            double xStep = Double.parseDouble(values[1]);
            double xUpper = Double.parseDouble(values[2]);
            List<Double> valuesArray = new ArrayList<>();
            for (int i = 0; i <= (int) ((xUpper - xLower) / xStep); i++)
                valuesArray.add(xLower + i * xStep);
            mapVariablesValues.put(variable, valuesArray);
        }
        return mapVariablesValues;
    }

    private double[][] getValuesTuples(String valuesKind, LinkedHashMap<String, List<Double>> mapVariablesValues) throws LengthListException {
        int n = mapVariablesValues.keySet().size();
        double[][] valuesTuplesT;
        if (valuesKind.equals(ValuesKind.LIST.getValue())) {
            int sumListsSize = 0;
            for (List<Double> tuple : mapVariablesValues.values()) {
                sumListsSize += tuple.size();
            }
            if (sumListsSize % n != 0)
                throw new LengthListException("Cannot resolve LIST, lists have different length");
            else {
                valuesTuplesT = new double[sumListsSize / n][n];
                for (int i = 0; i < (sumListsSize / n); i++) {
                    int j = 0;
                    for (List<Double> tuple : mapVariablesValues.values()) {
                        valuesTuplesT[i][j] = tuple.get(i);
                        j++;
                    }
                }
            }
        } else {
            //cartesian product
            int solutions = 1;
            for (List<Double> tuple : mapVariablesValues.values())
                solutions *= tuple.size();
            valuesTuplesT = new double[solutions][n];
            for (int i = 0; i < solutions; i++) {
                int j = 0;
                int k = 1;
                for (List<Double> list : mapVariablesValues.values()) {
                    valuesTuplesT[i][j] = list.get((i / k) % list.size());
                    k *= list.size();
                    j++;
                }
            }
        }
        return valuesTuplesT;
    }
}
