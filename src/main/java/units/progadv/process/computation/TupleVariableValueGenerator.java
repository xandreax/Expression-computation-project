package units.progadv.process.computation;

import units.progadv.exceptions.LengthListException;

import java.util.LinkedHashMap;
import java.util.List;

public class TupleVariableValueGenerator {

    private final String valuesKind;
    private final LinkedHashMap<String, List<Double>> mapVariablesValues;

    public TupleVariableValueGenerator(String valuesKind, LinkedHashMap<String, List<Double>> mapVariablesValues) {
        this.valuesKind = valuesKind;
        this.mapVariablesValues = mapVariablesValues;
    }

    public double[][] generateTuple () throws LengthListException {
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
