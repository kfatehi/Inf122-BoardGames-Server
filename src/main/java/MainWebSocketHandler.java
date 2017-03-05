import org.java_websocket.*;
import org.java_websocket.server.*;
import org.java_websocket.handshake.*;
import org.java_websocket.framing.*;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;


/**
 * Created by james on 3/5/17.
 */
public class MainWebSocketHandler extends WebSocketServer {

    public MainWebSocketHandler(int port) throws UnknownHostException {
        super(new InetSocketAddress(port));
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println( conn.getRemoteSocketAddress().getAddress().getHostAddress() + " entered the room!" );
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println( conn + " has left the room!" );
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println( conn + ": " + message );
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        ex.printStackTrace();
        if(conn != null) {
            // some errors like port binding failed may not be assignable to a specific websocket
        }
    }
}
