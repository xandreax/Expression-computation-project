package units.progadv.process.computation;

import units.progadv.exceptions.*;
import units.progadv.process.ExpressionResponse.ResultResponseFormatter;
import units.progadv.process.computation.ExpressionParser.Node;
import units.progadv.process.computation.ExpressionParser.Parser;

import java.util.*;

public class ComputationProcess {

    private final String lineInput;

    public ComputationProcess(String lineInput) {
        this.lineInput = lineInput;
    }

    public String compute() throws ComputationException, IllegalArgumentException {
        String[] lineTokens = requestParser(lineInput);
        String computationKind = lineTokens[0].substring(0, lineTokens[0].indexOf("_"));
        if (!ComputationKind.match(computationKind))
            throw new WrongComputationKindException(String.format(
                    "Wrong computation kind %s detected", computationKind
            ));
        String valuesKind = lineTokens[0].substring(lineTokens[0].indexOf("_") + 1);
        if (!ValuesKind.match(valuesKind))
            throw new WrongValuesKindException(String.format(
                    "Wrong values kind %s detected", valuesKind
            ));
        MapVariableValueGenerator mapGenerator = new MapVariableValueGenerator(lineTokens[1]);
        LinkedHashMap<String, List<Double>> mapVariablesValues = mapGenerator.generateMap();
        TupleVariableValueGenerator tupleGenerator = new TupleVariableValueGenerator(valuesKind, mapVariablesValues);
        List<List<Double>> tuples = tupleGenerator.generateTuple();
        Parser parser;
        List<Node> exprParsed = new ArrayList<>();
        for (int i = 2; i < lineTokens.length; i++) {
            parser = new Parser(lineTokens[i]);
            exprParsed.add(parser.parse());
        }
        List<Double> results = new ArrayList<>();
        ExpressionCalculator calculator;
        for (Node expr : exprParsed) {
            for (List<Double> tuple : tuples) {
                calculator = new ExpressionCalculator(tuple, mapVariablesValues);
                double result = calculator.calculate(expr);
                results.add(result);
            }
        }
        double finalResult;
        switch (computationKind) {
            case "MIN":
                int indexMin = results.indexOf(Collections.min(results));
                finalResult = results.get(indexMin);
                break;
            case "MAX":
                int indexMax = results.indexOf(Collections.max(results));
                finalResult = results.get(indexMax);
                break;
            case "AVG":
                double sumTot = 0;
                for (double n : results)
                    sumTot += n;
                finalResult = sumTot / results.size();
                break;
            case "COUNT":
                finalResult = results.size();
                break;
            default:
                throw new IllegalStateException(String.format("Unexpected computation kind: %s", computationKind));
        }
        return ResultResponseFormatter.formatResult(finalResult);
    }

    private static String[] requestParser(String lineInput) throws WrongRequestFormatException {
        String[] lineTokens;
        lineTokens = lineInput.split(";");
        if (lineTokens.length >= 3)
            return lineTokens;
        else
            throw new WrongRequestFormatException("Format error, please use this format: " +
                    "\"ComputationKind\"_\"ValuesKind\";\"VariableValuesFunction\";\"Expressions\"");
    }
}
