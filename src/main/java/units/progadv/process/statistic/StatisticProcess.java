package units.progadv.process.statistic;

import java.util.List;

public class StatisticProcess {

    private final String command;
    private final List<Float> computationTimesList;

    public StatisticProcess(String command, List<Float> computationTimesList) {
        this.command = command;
        this.computationTimesList = computationTimesList;
    }

    public String evaluate() {
        String response;
        if(command.equals(StatCommand.SUM_OF_REQUESTS.getValue())) {
            response = Integer.toString(computationTimesList.size());
        } else if(command.equals(StatCommand.AVERAGE_TIME_REQUESTS.getValue())) {
            float totalReqTime = 0;
            for (float req : computationTimesList) {
                totalReqTime = totalReqTime + req;
            }
            response = Float.toString(totalReqTime / computationTimesList.size());
        } else {
            float maxReqTime = 0;
            for (float req : computationTimesList) {
                if (req > maxReqTime)
                    maxReqTime = req;
            }
            response = Float.toString(maxReqTime);
        }
        return response;
    }
}
