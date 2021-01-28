package units.progadv.process.statistic;

public class ServerComputationTimeStatistic {

    private static ServerComputationTimeStatistic instance;
    private long totalNumberResponse;
    private double maxComputationTime;
    private double avgComputationTime;


    private ServerComputationTimeStatistic() {
        this.totalNumberResponse = 0;
        this.maxComputationTime = 0.0;
        this.avgComputationTime = 0.0;
    }

    public static synchronized ServerComputationTimeStatistic getThis() {
        if (instance == null) {
            instance = new ServerComputationTimeStatistic();
        }
        return instance;
    }

    public synchronized void addComputationTime(double computationTime) {
        this.totalNumberResponse++;
        if (computationTime > this.maxComputationTime) {
            maxComputationTime = computationTime;
        }
        this.avgComputationTime = ((computationTime - avgComputationTime) / totalNumberResponse) + avgComputationTime;
    }

    public synchronized long getTotalNumberResponse() {
        return totalNumberResponse;
    }

    public synchronized double getMaxComputationTime() {
        return maxComputationTime;
    }

    public synchronized double getAvgComputationTime() {
        return avgComputationTime;
    }
}
