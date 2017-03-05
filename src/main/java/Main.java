import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Created by james on 3/5/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        int port = 8080;
        MainWebSocketHandler ws = new MainWebSocketHandler(port);
        ws.start();
        System.out.println("WebSocket started on ws://localhost:" + port);

        BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String in = sysin.readLine();
        }
    }
}
