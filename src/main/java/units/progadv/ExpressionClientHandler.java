package units.progadv;

import units.progadv.process.ExpressionResponse.ErrorResponse;
import units.progadv.process.ExpressionResponse.ResultResponse;
import units.progadv.process.computation.ComputationProcess;
import units.progadv.process.statistic.StatCommand;
import units.progadv.process.statistic.StatisticProcess;

import java.io.*;
import java.net.Socket;

public class ExpressionClientHandler extends Thread {

    private final Socket socket;
    private final ExpressionServer server;
    private final PrintStream ps;

    public ExpressionClientHandler(Socket socket, ExpressionServer server, PrintStream ps) {
        this.socket = socket;
        this.server = server;
        this.ps = ps;
    }

    public void run() {
        ps.printf(" [%1$tY-%1$tm-%1$td %1$tT] Connection from %2$s .%n", System.currentTimeMillis(), socket.getInetAddress());
        int requestsCounter = 0;
        long endTime, startTime;
        float computationTime;

        try(socket) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String[] result;
                String line = br.readLine();
                startTime = System.currentTimeMillis();
                if(line == null){
                    System.err.println("Empty line input, closed connection");
                    break;
                }
                else if (line.equals(server.getQuitCommand())) {
                    break;
                } else if (StatCommand.match(line)){
                    StatisticProcess statistic = new StatisticProcess(line, server.getComputationTimes());
                    result = statistic.evaluate();
                } else {
                    ComputationProcess computation = new ComputationProcess(line);
                    result = computation.compute();
                }
                if(result[0].equals("false")) {
                    endTime = System.currentTimeMillis();
                    computationTime = (float) (endTime - startTime) / 1000;
                    server.addComputationTime(computationTime);
                    System.out.println(result[1]);//test
                    bw.write(ResultResponse.create(result[1], computationTime) + System.lineSeparator());
                    bw.flush();
                    requestsCounter = requestsCounter + 1;
                }
                else {
                    bw.write(ErrorResponse.create(result[1]) + System.lineSeparator());
                    bw.flush();
                }
            }
        } catch (Exception e) {
            System.err.printf("IO exception: %s", e);
        }
        try {
            socket.close();
            ps.printf(" [%1$tY-%1$tm-%1$td %1$tT] Disconnection of %2$s after %3$d requests .%n",
                    System.currentTimeMillis(), socket.getInetAddress(), requestsCounter);
        } catch (IOException e) {
            System.err.printf("IO exception: %s", e);
        }
    }
}
