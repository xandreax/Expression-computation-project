import java.util.List;

public class StatisticProcess {

    private final String command;
    private final List<Long> computationTimesList;

    public StatisticProcess(String command, List<Long> computationTimesList) {
        this.command = command;
        this.computationTimesList = computationTimesList;
    }

    public String evaluate() {
        String response;
        if(command.equals(StatCommand.SUM_OF_REQUESTS.getValue())) {
            response = Integer.toString(computationTimesList.size());
            System.out.println(response);
        } else if(command.equals(StatCommand.AVERAGE_TIME_REQUESTS.getValue())) {
            long totalReqTime = 0;
            for (long req : computationTimesList) {
                totalReqTime = totalReqTime + req;
            }
            response = Long.toString(totalReqTime / computationTimesList.size());
            System.out.println(response);
        } else {
            long maxReqTime = 0;
            for (long req : computationTimesList) {
                if (req > maxReqTime)
                    maxReqTime = req;
            }
            response = Long.toString(maxReqTime);
            System.out.println(response);
        }
        return response;
    }
}
