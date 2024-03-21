import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        List<String> arguments = Arrays.asList(args);

        String directory = ".";
        if(arguments.contains("--directory")) {
            directory = arguments.get(arguments.indexOf("--directory") +1);
        }
        new TCPServer(directory).start();
    }
}