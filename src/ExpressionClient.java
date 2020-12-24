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
        DataInputStream dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        DataOutputStream dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("Ready to send");
            try {
                String sent = scanner.next();
                System.out.println(sent);
                dos.writeBytes(sent + System.lineSeparator());
                System.out.println("Sent!");
                dos.flush();
                System.out.println("Flush!");
                String received = String.valueOf(dis.read());
                System.out.println(received);
                System.out.println("Captured ExpressionResponse.Response!");
                System.out.printf("Sent: %s%nReceived: %s%n", sent, received);
                if (sent.equals("BYE"))
                    break;
            } catch (IOException e) {
                e.printStackTrace();
            }
            //dos.writeBytes("BYE" + System.lineSeparator());
            //dos.flush();
            //socket.close();
        }
        socket.close();
    }
}