public enum StatCommand {
    SUM_OF_REQUESTS("STAT_REQS"),
    AVERAGE_TIME_REQUESTS("STAT_AVG_TIME"),
    MAX_TIME_REQUEST("STAT_MAX_TIME");
    private final String statCommand;

    StatCommand (String statCommand){
        this.statCommand = statCommand;
    }

    public static boolean match (String line){
        for(StatCommand statCommandValue : StatCommand.values()){
            if(statCommandValue.statCommand.equals(line))
                return true;
        }
        return false;
    }

    public String getValue(){
        return statCommand;
    }
}
