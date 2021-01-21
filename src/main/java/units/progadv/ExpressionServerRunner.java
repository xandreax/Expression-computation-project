package units.progadv;

import java.io.IOException;

public class ExpressionServerRunner {

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
