import java.io.*;
import java.net.Socket;
import java.util.List;

public class ExpressionClientHandler extends Thread {

    private final Socket socket;
    private final ExpressionServer server;
    private final PrintStream ps;
    private static boolean computationError = false;
    private static String errorDescription;

    public ExpressionClientHandler(Socket socket, ExpressionServer server, PrintStream ps) {
        this.socket = socket;
        this.server = server;
        this.ps = ps;
    }

    public void run() {
        ps.printf(" [%1$tY-%1$tm-%1$td %1$tT] Connection from %2$s .%n", System.currentTimeMillis(), socket.getInetAddress());
        int requestsCounter = 0;
        long endTime, computationTime, startTime;
        try(socket) {
            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            while (true) {
                System.out.println("Ready to receive");
                String line = br.readLine();
                String result;
                startTime = System.currentTimeMillis();
                if(line == null){
                    System.err.println("Client abruptly closed connection");
                    break;
                }
                else if (line.equals(server.getQuitCommand())) {
                    break;
                } else if (StatCommand.match(line)){
                    result = statistic(line,server.getComputationTimes());
                } else {
                    ComputationProcess computation = new ComputationProcess(line);
                    result = computation.evaluate();
                }
                endTime = System.currentTimeMillis();
                computationTime = endTime - startTime;
                server.addComputationTime(computationTime);
                bw.write(ResultResponse.create(result, computationTime) + System.lineSeparator());
                bw.flush();
                requestsCounter = requestsCounter + 1;
                ps.printf("Computation done in %.3f seconds", (float)(computationTime / 1000));
            }
        } catch (IOException e) {
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

    protected String statistic(String input, List<Long> computationTimes) {
        String response;
        if (input.equals(server.getStatCommand(0))) {
            response = Integer.toString(computationTimes.size());
            System.out.println(response);
        } else if (input.equals(server.getStatCommand(1))) {
            long totalReqTime = 0;
            for (long req : computationTimes) {
                totalReqTime = totalReqTime + req;
            }
            response = Long.toString(totalReqTime / computationTimes.size());
            System.out.println(response);
        } else {
            long maxReqTime = 0;
            for (long req : computationTimes) {
                if (req > maxReqTime)
                    maxReqTime = req;
            }
            response = Long.toString(maxReqTime);
            System.out.println(response);
        }
        return response;
    }
}
