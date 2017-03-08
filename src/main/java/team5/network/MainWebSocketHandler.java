package team5.network;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;


/**
 * Created by james on 3/5/17.
 */
@WebSocket
public class MainWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session session) throws Exception {
        System.out.println("New connection: " + session.getRemoteAddress());
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        System.out.println("Closed connection: " + statusCode + " " + session.getRemoteAddress() + " " + reason);

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) {
        System.out.println("New message: " + session.getRemoteAddress() + " " + message);
    }

}
