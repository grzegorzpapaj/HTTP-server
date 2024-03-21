import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServer {

    private String directory;
    public TCPServer(String directory) {
        this.directory = directory;
    }

    public void start() {

        ServerSocket serverSocket = null;
        Socket clientSocket = null;
        RequestHandler requestHandler = new RequestHandler();

        try {
            serverSocket = new ServerSocket(4221);
            serverSocket.setReuseAddress(true);

            while(true) {
                clientSocket = serverSocket.accept();
                System.out.println("Accepted a new connection");

                ClientHandler clientHandlerThread = new ClientHandler(clientSocket, requestHandler, directory);
                clientHandlerThread.start();
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
