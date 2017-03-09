package team5.game;

import org.eclipse.jetty.websocket.api.Session;
/**
 * Created by james on 3/7/17.
 */
public class GameManagerSingleton {
    public GameManagerSingleton() {
        System.out.println("GameManagerSingleton Comin in hot.");
    }

    public static boolean login(Session ws) {
        return true;
    }
}
