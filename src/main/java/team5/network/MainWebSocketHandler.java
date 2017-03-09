package team5.network;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;
import team5.game.GameManagerSingleton;
import team5.network.CommunicationBridge;


/**
 * Created by james on 3/5/17.
 */
@WebSocket
public class MainWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("New connection: " + session.getRemoteAddress());

        // Have the GameManagerSingleton create a new CommBridge for us,
        // passing our Session to it
        GameManagerSingleton.instance().newCommBridge(session);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("Closed connection: " + statusCode + " " + session.getRemoteAddress() + " " + reason);

        // Have the singleton lose the CommBridge
        GameManagerSingleton.instance().destroyCommBridge(session);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("New message: " + session.getRemoteAddress() + " " + message);

        // Get the CommBridge
        CommunicationBridge commBridge = GameManagerSingleton.instance().commBridge(session);

        // Pass on the JSON message (still as String)
        // test driver for login
        commBridge.parseMessage(message);
    }
}
