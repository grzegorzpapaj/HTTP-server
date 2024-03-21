import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestHandler {
    private final String RESPONSE_200_OK = "HTTP/1.1 200 OK\r\n";
    private final String RESPONSE_201_CREATED = "HTTP/1.1 201 CREATED\r\n";
    private final String RESPONSE_400_NOT_FOUND = "HTTP/1.1 404 Not Found\r\n";
    private final String RESPONSE_507_INSUFFICIENT_STORAGE = "HTTP/1.1 507 Insufficient Storage\r\n";
    private final String ENDL = "\r\n";

    public String readRequestFromBufferedReader(BufferedReader bufferedReader) throws IOException {

        StringBuilder requestBuilder = new StringBuilder();
        String line;

        while((line = bufferedReader.readLine()) != null && !line.isEmpty()) {
            requestBuilder.append(line).append("\r\n");
        }

        requestBuilder.append("\r\n");

        while(bufferedReader.ready()) {
            requestBuilder.append((char) bufferedReader.read());
        }

        return requestBuilder.toString();
    }

    public String executeRequest(String requestType, String request, String directory) throws IOException {
        if(requestType.equals("GET")) {
            return getResponse(request, directory);
        } else if(requestType.equals("POST")) {
            return postResponse(request, directory);
        }

        return RESPONSE_400_NOT_FOUND + ENDL;
    }

    private String getResponse(String request, String directory) throws IOException {

        String path = extractPathFromRequest(request);

        if(path.equals("/")) {
            return RESPONSE_200_OK + ENDL;

        } else if(path.matches("/echo/(.*)")) {
            String content = path.substring(6);
            String contentType = "text/plain";

            return getResponseStringWithContentIf200(content, contentType);

        } else if(path.equals("/user-agent")) {

            //get user agent from the request
            String userAgent = request.split("\n")[2].split(":")[1].trim();
            String contentType = "text/plain";

            return getResponseStringWithContentIf200(userAgent, contentType);
        } else if(path.matches("/files/(.*)")) {
            String filename = path.substring(7);
            Path filepath = Paths.get(directory + "/" + filename);

            if(Files.exists(filepath)) {
                String contentType = "application/octet-stream";

                String content = new String(Files.readAllBytes(filepath));

                return getResponseStringWithContentIf200(content, contentType);
            } else {
                return RESPONSE_400_NOT_FOUND + ENDL;
            }
        }
        else {
            return RESPONSE_400_NOT_FOUND + ENDL;
        }
    }

    private String postResponse(String request, String directory) throws IOException {
        String path = extractPathFromRequest(request);

        if(path.matches("/files/(.*)")) {
            String filename = path.substring(7);
            String content = request.split("\r\n\r\n")[1];

            try {
                Path directoryPath = Paths.get(directory);
                Path filePath = directoryPath.resolve(filename);

                Files.createDirectories(directoryPath);
                Files.createFile(filePath);

                Files.write(filePath, content.getBytes());

                return RESPONSE_201_CREATED + ENDL;

            } catch (FileAlreadyExistsException e) {

                return RESPONSE_507_INSUFFICIENT_STORAGE + ENDL;
            }
        }

        return RESPONSE_400_NOT_FOUND;
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
