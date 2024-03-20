import java.io.BufferedReader;
import java.io.IOException;

public class RequestHandler {

    public String readRequestFromBufferedReader(BufferedReader reader) throws IOException {

        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }

        return requestBuilder.toString();
    }
}
