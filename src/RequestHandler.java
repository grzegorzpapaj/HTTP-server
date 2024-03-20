import java.io.BufferedReader;
import java.io.IOException;

public class RequestHandler {

    private final String RESPONSE_400_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    private final String RESPONSE_200_OK = "HTTP/1.1 200 OK\r\n";
    private final String ENDL = "\r\n";

    public String readRequestFromBufferedReader(BufferedReader reader) throws IOException {

        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while((line = reader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }

        return requestBuilder.toString();
    }

    public String executeRequest(String requestType, String request) throws IOException {
        if(requestType.equals("GET")) {
            return getResponse(request);
        }

        return RESPONSE_400_NOT_FOUND + ENDL;
    }

    private String getResponse(String request) {

        String path = extractPathFromRequest(request);

        if(path.equals("/")) {
            return RESPONSE_200_OK + ENDL;

        } else if(path.matches("/echo/(.*)")) {
            String content = path.substring(6);
            String contentType = "text/plain";

            return getResponseStringWithContentIf200(content, contentType);

        } else {
            return RESPONSE_400_NOT_FOUND + ENDL;
        }
    }

    private String getResponseStringWithContentIf200(String content, String contentType) {

        return RESPONSE_200_OK +
                "Content-Type: " + contentType + ENDL +
                "Content-Length: " + content.length() + ENDL + ENDL +
                content + ENDL;
    }

    private String extractPathFromRequest(String request) {
        String startLine = request.split("\n")[0];
        String path = startLine.split(" ")[1];

        return path;
    }
}
