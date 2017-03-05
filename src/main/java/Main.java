import static spark.Spark.*;

/**
 * Created by james on 3/5/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        webSocket("/games", MainWebSocketHandler.class);
        init();
    }
}
