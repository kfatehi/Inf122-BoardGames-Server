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

    public boolean login(String username, CommunicationBridge communicationBridge) {

        User user = user(username);
        if (user == null) {
            users.add(new User(username));
        } else {
            // user exists, it's probably trying to resume...

            gamesInProgress.forEach(g->{
                g.getUsernames().forEach(u->{
                    if (username.equals(u)) {
                        // im in this game, update my bridge
                        g.updateBridge(u, communicationBridge);
                        communicationBridge.setGameSession(g);
                        communicationBridge.setUsername(username);
                        g.userRejoined(u); // Resend game init/game state change
                    }
                });
            });
        }

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
        if (commBridge.username() == null || commBridge.username().equals("")) {
            // Don't create if they're not logged in
            return null;
        }

        GameSession game = new GameSession(gameName, pugName, commBridge.username());
        game.addUser(commBridge.username(), commBridge);
        gamesWaiting.add(game);

        broadcastOpenGames();
        return game;
    }

    public GameSession joinGameSession(CommunicationBridge commBridge, final int gameSessionID) {
        if (commBridge.username() == null || commBridge.username().equals("")) {
            // Don't join if they're not logged in
            return null;
        }

        // Find the game index
        int index = -1;
        for (int i = 0; i < gamesWaiting.size(); i++) {
            if (gamesWaiting.get(i).id() == gameSessionID) {
                index = i;
                break;
            }
        }
        // No game, exit
        if (index == -1) { return null; }

        // Add the user to the game
        GameSession game = gamesWaiting.get(index);
        game.addUser(commBridge.username(), commBridge);

        // Set joining users commBridge with game session
        commBridge.setGameSession(game);

        // If the game has everyone, move it to inProgress and start it.
        if (game.isFull()) {
            gamesWaiting.remove(index);
            gamesInProgress.add(game);

            game.start();
        }

        broadcastOpenGames();
        return game;
    }

    public void destroyGameSession(GameSession gameSession) {
        gamesInProgress.remove(gameSession);

        // Disassociate the bridges from the game session
        userSessions.forEach((s, cb) -> {
            gameSession.getUsernames().forEach(u-> {
                if (cb.username().equals(u)) {
                    cb.setGameSession(null);
                }
            });
        });

    }

    private void broadcastOpenGames() {
        for (CommunicationBridge commBridge : userSessions.values()) {
            commBridge.listOpenGamesHandler(null);
        }
    }

    public void userWon(String username, String gameName) {
        this.user(username).gameStat(gameName).incrementWins();
    }

    public void userLost(String username, String gameName) {
        this.user(username).gameStat(gameName).incrementLosses();
    }

    public void userDraw(String username, String gameName) {
        this.user(username).gameStat(gameName).incrementDraws();
    }

    public List<GameSession> getGamesWaiting() { return gamesWaiting; }
    public List<GameSession> getGamesInProgress() { return gamesInProgress; }
}
