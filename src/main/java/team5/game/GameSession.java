package team5.game;

import team5.game.state.GameState;
import team5.network.CommunicationBridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 3/7/17.
 */
public class GameSession {

    private int id;
    private String pugName;

    private List<String> usernames = new ArrayList<String>();
    private Map<String, CommunicationBridge> commBridges = new HashMap<String, CommunicationBridge>();
    private GameState gameState;
    private GameLogic gameLogic;

    public GameSession(){

    }
    public GameSession(String gameName, String pugName) {
        System.out.println("Building GameSession");


    }

    public void addUser(String username, CommunicationBridge commBridge) {
        usernames.add(username);
        commBridges.put(username, commBridge);
    }

    public int id() { return id; }
    public String pugName() { return pugName; }
    public String gameName() { return gameLogic.gameName(); }
    public List<String> getUsernames() { return usernames; }
}
