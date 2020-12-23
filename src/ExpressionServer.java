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
    private final List<Long> computationTimes = new ArrayList<>();
    private final ExecutorService executorService;

    public ExpressionServer(int port, OutputStream os, int concurrentClients) {
        this.port = port;
        ps = new PrintStream(os);
        executorService = Executors.newFixedThreadPool(concurrentClients);
    }

    public void start() throws IOException {
        try(ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    executorService.submit(() -> {
                        ExpressionClientHandler exprClientHandler = new ExpressionClientHandler(socket, this, ps);
                        exprClientHandler.start();
                    });
                } catch (IOException e) {
                    System.err.printf("Cannot accept connection due to %s", e);
                }
            }
        }
        finally {
            executorService.shutdown();
        }
    }

    public String getQuitCommand() {
        return "BYE";
    }

    public List<Long> getComputationTimes(){
        return computationTimes;
    }

    public void addComputationTime (long computationTime) {
        computationTimes.add(computationTime);
    }

    public static void main(String[] args) throws IOException {
        if (args.length < 1) {
            System.out.println("No arguments");
            System.exit(0);
        }
        if (!parameterIsInt(args[0]))
            throw new NumberFormatException("The given number is not an integer");
        int port = Integer.parseInt(args[0]);
        int cores = Runtime.getRuntime().availableProcessors();
        System.out.printf("The available cores are: %d%n", cores);
        ExpressionServer server = new ExpressionServer(port, System.out, cores);
        server.start();
    }

    private static boolean parameterIsInt(String parameter) {
        try {
            Integer.parseInt(parameter);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

