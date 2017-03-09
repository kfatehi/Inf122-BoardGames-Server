package team5.game;

import org.eclipse.jetty.websocket.api.Session;
import team5.network.CommunicationBridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 3/7/17.
 */
public class GameManagerSingleton {
    private static GameManagerSingleton instance;

    private List<GameSession> gamesWaiting = new ArrayList<GameSession>();
    private List<GameSession> gamesInProgress = new ArrayList<GameSession>();
    private List<User> users = new ArrayList<User>();
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

    public User user(String username) {
        for (User user : users) {
            if (user.name().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public GameSession createGameSession(CommunicationBridge commBridge, String gameName, String pugName) {
        // TODO: when GameSession initializer is done, pass in gameName and pugName, and commBridge
        GameSession game = new GameSession();
        gamesWaiting.add(game);
        return game;
    }

    public GameSession joinGameSession(CommunicationBridge commBridge, final int gameSessionID) {
        int index = -1;
        for (int i = 0; i < gamesWaiting.size(); i++) {
            if (gamesWaiting.get(i).id() == gameSessionID) {
                index = i;
                break;
            }
        }
        if (index == -1) { return null; }

        GameSession game = gamesWaiting.remove(index);
        gamesInProgress.add(game);

        // TODO: Indicate to the game that it is full? Or should that be in the logic/sesison itself.

        return game;
    }

    public void userWon(String username, String gameName) {

    }

    public void userLost(String username, String gameName) {

    }

    public void userDraw(String username, String gameName) {

    }
}
