import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try (ServerSocket server = new ServerSocket(8899)) {
                    while (true) {
                        Socket s = server.accept();
                        ObjectInputStream ois =
                                new ObjectInputStream(s.getInputStream());
                        String msg = (String) ois.readObject();
                        System.out.println(String.format(
                                "Received '%s'",
                                msg));
                        ois.close();
                        s.close();
                        if ("exit".equals(msg)) {
                            System.out.println("Server exited;");
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
        while (true) {
            System.out.println("Enter message:");
            String msg = new Scanner(System.in).nextLine();
            try (Socket client = new Socket("localhost", 8899)) {
                ObjectOutputStream oos = new ObjectOutputStream(
                        client.getOutputStream());
                oos.writeObject(msg);
                oos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ("exit".equals(msg)) {
                System.out.println("Client exited;");
                break;
            }
        }

    }
}
