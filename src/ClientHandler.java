import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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

            String requestType = request.split(" ")[0];

            String response = requestHandler.executeRequest(requestType, request);
            System.out.println("Writing response:\n" + response);
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));

            bufferedReader.close();
            outputStream.close();
            clientSocket.close();;

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
