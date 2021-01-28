package units.progadv.process.statistic;

import units.progadv.process.ExpressionResponse.ResultResponseFormatter;

import java.util.List;

public class StatisticProcess {

    private final String command;
    private final List<Double> computationTimesList;

    public StatisticProcess(String command, List<Double> computationTimesList) {
        this.command = command;
        this.computationTimesList = computationTimesList;
    }

    public String evaluate() {
        double response;
        if(command.equals(StatCommand.SUM_OF_REQUESTS.getValue())) {
            response = computationTimesList.size();
        } else if(command.equals(StatCommand.AVERAGE_TIME_REQUESTS.getValue())) {
            double totalReqTime = 0;
            for (Double req : computationTimesList) {
                totalReqTime = totalReqTime + req;
            }
            response = totalReqTime / computationTimesList.size();
        } else {
            double maxReqTime = 0;
            for (double req : computationTimesList) {
                if (req > maxReqTime)
                    maxReqTime = req;
            }
            response = maxReqTime;
        }
        return ResultResponseFormatter.formatResult(response);
    }
}
