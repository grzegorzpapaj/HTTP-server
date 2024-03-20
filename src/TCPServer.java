import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    public void start() {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            clientSocket = serverSocket.accept();
            System.out.println("accepted a connection");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
