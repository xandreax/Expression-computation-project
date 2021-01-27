package units.progadv.process.computation;

import units.progadv.exceptions.WrongFormatVariableValueFunctionException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class MapVariableValueGenerator {

    private final String variablesValuesFunction;

    public MapVariableValueGenerator(String variablesValuesFunction) {
        this.variablesValuesFunction = variablesValuesFunction;
    }

    public LinkedHashMap<String, List<Double>> generateMap() throws WrongFormatVariableValueFunctionException,
            NumberFormatException {
        LinkedHashMap<String, List<Double>> mapVariablesValues = new LinkedHashMap<>();
        String[] variablesValuesFunctionTokens = variablesValuesFunction.split(",");
        for (String variableValuesFunction : variablesValuesFunctionTokens) {
            String[] variablesValuesTokens = variableValuesFunction.split(":");
            if (variablesValuesTokens.length != 4)
                throw new WrongFormatVariableValueFunctionException("Format error in VariableValueFunction, " +
                        "please use this format: VarName:JavaNum:JavaNum:JavaNum");
            String variable = variablesValuesTokens[0];
            if (valueIsNotDouble(variablesValuesTokens[1]))
                throw new NumberFormatException(String.format("Error in VariableValueFunction: %s is not a number",
                        variablesValuesTokens[1]));
            if (valueIsNotDouble(variablesValuesTokens[2]))
                throw new NumberFormatException(String.format("Error in VariableValueFunction: %s is not a number",
                        variablesValuesTokens[2]));
            if (valueIsNotDouble(variablesValuesTokens[3]))
                throw new NumberFormatException(String.format("Error in VariableValueFunction: %s is not a number",
                        variablesValuesTokens[3]));
            double xLower = Double.parseDouble(variablesValuesTokens[1]);
            double xStep = Double.parseDouble(variablesValuesTokens[2]);
            double xUpper = Double.parseDouble(variablesValuesTokens[3]);
            List<Double> valuesArray = new ArrayList<>();
            for (int i = 0; i <= (int) ((xUpper - xLower) / xStep); i++)
                valuesArray.add(xLower + i * xStep);
            mapVariablesValues.put(variable, valuesArray);
        }
        return mapVariablesValues;
    }

    private static boolean valueIsNotDouble(String value) {
        try {
            Double.parseDouble(value);
            return false;
        } catch (NumberFormatException e) {
            return true;
        }
    }
}
