package units.progadv;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class ExpressionClient {
    public static void main(String[] args) throws IOException {
        InetAddress serverIneptAddress;
        if (args.length > 1) {
            serverIneptAddress = InetAddress.getByName(args[0]);
        } else {
            serverIneptAddress = InetAddress.getLocalHost();
        }
        Socket socket = new Socket(serverIneptAddress, 10000);
        System.out.println("Connection...");
        BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ready to send");
            try {
                String sent = scanner.next();
                System.out.println(sent);
                bw.write(sent + System.lineSeparator());
                System.out.println("Sent!");
                bw.flush();
                System.out.println("Flush!");
                String result = br.readLine();
                System.out.printf("Sent: %s%nReceived: %s%n", sent, result);
                if (sent.equals("BYE"))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        socket.close();
    }
}