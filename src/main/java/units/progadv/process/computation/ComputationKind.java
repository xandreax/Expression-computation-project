package units.progadv.process.computation;

public enum ComputationKind {
    MINIMUM("MIN"),
    MAXIMUM("MAX"),
    AVERAGE("AVG"),
    COUNT("COUNT");

    private final String computationKind;

    ComputationKind (String computationKind){
        this.computationKind = computationKind;
    }

    public static boolean match (String line){
        for(ComputationKind computationKindValue : ComputationKind.values()){
            if(computationKindValue.computationKind.equals(line))
                return true;
        }
        return false;
    }

    public String getValue(){
        return computationKind;
    }
}
