package units.progadv;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExpressionServer {

    private final int port;
    private final PrintStream ps;
    private final List<Double> computationTimes;
    private final ExecutorService executorService;

    public ExpressionServer(int port, OutputStream os, int concurrentClients) {
        this.port = port;
        ps = new PrintStream(os);
        executorService = Executors.newFixedThreadPool(concurrentClients);
        computationTimes = new ArrayList<>();
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket socket = serverSocket.accept();
                executorService.submit(() -> {
                    ExpressionClientHandler exprClientHandler = new ExpressionClientHandler(socket, this, ps);
                    exprClientHandler.start();
                });
            }
        } catch (IOException e) {
            System.err.printf("Cannot accept connection due to %s", e);
        } finally {
            executorService.shutdown();
        }
    }

    public String getQuitCommand() {
        return "BYE";
    }

    public List<Double> getComputationTimes() {
        return computationTimes;
    }

    public synchronized void addComputationTime(double computationTime) {
        computationTimes.add(computationTime);
    }

}

