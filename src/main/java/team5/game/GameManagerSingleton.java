package team5.game;

import org.eclipse.jetty.websocket.api.Session;
import team5.network.CommunicationBridge;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 3/7/17.
 */
public class GameManagerSingleton {
    private static GameManagerSingleton instance;

    private Map<Session, CommunicationBridge> userSessions = new HashMap<Session, CommunicationBridge>();

    private GameManagerSingleton() {
        System.out.println("GameManagerSingleton Comin in hot.");
    }

    public static GameManagerSingleton instance() {
        if (instance == null) {
            instance = new GameManagerSingleton();
        }
        return instance;
    }

    public void newCommBridge(Session session) {
        CommunicationBridge commBridge = new CommunicationBridge(session);
        userSessions.put(session, commBridge);
    }

    public CommunicationBridge commBridge(Session session) {
        return userSessions.get(session);
    }

    public void destroyCommBridge(Session session) {
        userSessions.remove(session);
    }

    public boolean login(String username) {
        return true;
    }
}
