package units.progadv.process.computation;

import units.progadv.exceptions.LengthListException;

import java.util.*;

public class TupleVariableValueGenerator {

    private final String valuesKind;
    private final LinkedHashMap<String, List<Double>> mapVariablesValues;

    public TupleVariableValueGenerator(String valuesKind, LinkedHashMap<String, List<Double>> mapVariablesValues) {
        this.valuesKind = valuesKind;
        this.mapVariablesValues = mapVariablesValues;
    }

    public List<List<Double>> generateTuple() throws LengthListException {
        int n = mapVariablesValues.keySet().size();
        List<List<Double>> tuples;
        if (valuesKind.equals(ValuesKind.LIST.getValue())) {
            int sumListsSize = 0;
            for (List<Double> variableValues : mapVariablesValues.values()) {
                sumListsSize += variableValues.size();
            }
            if (n != 1 && sumListsSize % n != n)
                throw new LengthListException("Cannot resolve LIST, lists have different length");
            else {
                tuples = new ArrayList<>();
                for (int i = 0; i < (sumListsSize / n); i++) {
                    List<Double> tuple = new ArrayList<>();
                    for (List<Double> variableValues : mapVariablesValues.values()) {
                        tuple.add(variableValues.get(i));
                    }
                    tuples.add(tuple);
                }
            }
        } else {
            //cartesian product
            tuples = Arrays.asList(Arrays.asList());
            for (List<Double> variableValues : mapVariablesValues.values()) {
                List<List<Double>> extraTuples = new ArrayList<>();
                for (List<Double> tuple : tuples) {
                    for (double value : variableValues) {
                        List<Double> newTuple = new ArrayList<>(tuple);
                        newTuple.add(value);
                        extraTuples.add(newTuple);
                    }
                }
                tuples = extraTuples;
            }
        }
        return tuples;
    }
}
