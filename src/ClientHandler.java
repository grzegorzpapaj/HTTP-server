import java.io.*;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket clientSocket;
    private RequestHandler requestHandler;

    public ClientHandler(Socket clientSocket, RequestHandler requestHandler) {
        this.clientSocket = clientSocket;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        try {
            OutputStream outputStream = clientSocket.getOutputStream();
            InputStream inputStream = clientSocket.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String request = requestHandler.readRequestFromBufferedReader(bufferedReader);
            System.out.println("Received request:\n " + request);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
