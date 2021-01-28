package units.progadv.process.statistic;

import units.progadv.process.ExpressionResponse.ResultResponseFormatter;

public class StatisticProcess {

    private final String command;

    public StatisticProcess(String command) {
        this.command = command;
    }

    public String evaluate() {
        double response;
        if (command.equals(StatCommand.SUM_OF_REQUESTS.getValue())) {
            response = ServerComputationTimeStatistic.getThis().getTotalNumberResponse();
        } else if (command.equals(StatCommand.AVERAGE_TIME_REQUESTS.getValue())) {
            response = ServerComputationTimeStatistic.getThis().getAvgComputationTime();
        } else {
            response = ServerComputationTimeStatistic.getThis().getMaxComputationTime();
        }
        return ResultResponseFormatter.formatResult(response);
    }
}
