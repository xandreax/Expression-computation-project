public enum ValuesKind {
    GRID("GRID"),
    LIST("LIST");

    private final String valuesKind;

    ValuesKind(String valuesKind){
        this.valuesKind = valuesKind;
    }

    public static boolean match (String line){
        for(ValuesKind valuesKindValue : ValuesKind.values()){
            if(valuesKindValue.valuesKind.equals(line))
                return true;
        }
        return false;
    }

    public String getValue(){
        return valuesKind;
    }
}
