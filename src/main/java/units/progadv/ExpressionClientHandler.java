package units.progadv;

import units.progadv.process.ExpressionResponse.Response;
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
        long startTime;
        Response response;
        float computationTime;

        try(socket) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                String result;
                String line = br.readLine();
                startTime = System.currentTimeMillis();
                if(line == null){
                    System.err.printf("Empty line input, closed connection .%n");
                    break;
                }
                else if (line.equals(server.getQuitCommand())) {
                    break;
                } else if (StatCommand.match(line)){
                    StatisticProcess statistic = new StatisticProcess(line, server.getComputationTimes());
                    result = statistic.evaluate();
                    computationTime = calculateComputationTime(startTime);
                    response = new Response(result, computationTime, Response.ResponseType.OK);
                } else {
                    try {
                        ComputationProcess computation = new ComputationProcess(line);
                        result = computation.compute();
                        computationTime = calculateComputationTime(startTime);
                        response = new Response(result, computationTime, Response.ResponseType.OK);
                    }
                    catch(Exception e){
                        result = e.getMessage();
                        computationTime = calculateComputationTime(startTime);
                        response = new Response(result, computationTime, Response.ResponseType.ERR);
                    }
                }
                server.addComputationTime(computationTime);
                bw.write( response.toString() + System.lineSeparator());
                bw.flush();
                requestsCounter = requestsCounter + 1;
            }
            socket.close();
            ps.printf(" [%1$tY-%1$tm-%1$td %1$tT] Disconnection of %2$s after %3$d requests .%n",
                    System.currentTimeMillis(), socket.getInetAddress(), requestsCounter);
        } catch (IOException e) {
            System.err.printf("IO exception: %s", e);
        }
    }

    private static float calculateComputationTime (long startTime){
        long endTime = System.currentTimeMillis();
        return (float) (endTime - startTime) / 1000;
    }
}
