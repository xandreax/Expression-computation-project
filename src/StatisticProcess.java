import java.util.List;

public class StatisticProcess {

    private final String command;
    private final List<Float> computationTimesList;

    public StatisticProcess(String command, List<Float> computationTimesList) {
        this.command = command;
        this.computationTimesList = computationTimesList;
    }

    public String[] evaluate() {
        String[] response = new String[2];
        response[0] = "false";
        if(command.equals(StatCommand.SUM_OF_REQUESTS.getValue())) {
            response[1] = Integer.toString(computationTimesList.size());
            System.out.println(response[1]);//test
        } else if(command.equals(StatCommand.AVERAGE_TIME_REQUESTS.getValue())) {
            float totalReqTime = 0;
            for (float req : computationTimesList) {
                totalReqTime = totalReqTime + req;
            }
            response[1] = Float.toString(totalReqTime / computationTimesList.size());
            System.out.println(response[1]);//test
        } else {
            float maxReqTime = 0;
            for (float req : computationTimesList) {
                if (req > maxReqTime)
                    maxReqTime = req;
            }
            response[1] = Float.toString(maxReqTime);
            System.out.println(response[1]);//test
        }
        return response;
    }
}
