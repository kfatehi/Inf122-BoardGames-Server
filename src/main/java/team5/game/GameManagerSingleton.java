package team5.game;

import org.eclipse.jetty.websocket.api.Session;
/**
 * Created by james on 3/7/17.
 */
public class GameManagerSingleton {
    private static GameManagerSingleton instance;

    private GameManagerSingleton() {
        System.out.println("GameManagerSingleton Comin in hot.");
    }

    public static GameManagerSingleton instance() {
        if (instance == null) {
            instance = new GameManagerSingleton();
        }
        return instance;
    }

    public boolean login(String username) {
        return true;
    }
}
