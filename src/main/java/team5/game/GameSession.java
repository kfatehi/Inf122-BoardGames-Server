package team5.game;

import team5.game.state.GameState;
import team5.game.state.PieceCoordinate;
import team5.network.CommunicationBridge;
import team5.plugins.chess.ChessGameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 3/7/17.
 */
public class GameSession {

    private static int nextId = 0;

    private int id;
    private String pugName;

    private List<String> usernames = new ArrayList<String>();
    private Map<String, CommunicationBridge> commBridges = new HashMap<String, CommunicationBridge>();
    private GameState gameState;
    private GameLogic gameLogic;

    public GameSession(){
        id = nextId;
        nextId += 1;
    }
    public GameSession(String gameName, String pugName) {
        System.out.println("Building GameSession");

        id = nextId;
        nextId += 1;

        this.pugName = pugName;

        // Temp
        gameLogic = new ChessGameLogic();

    }

    public void addUser(String username, CommunicationBridge commBridge) {
        usernames.add(username);
        commBridges.put(username, commBridge);
    }

    public boolean isFull() {
        return usernames.size() >= GameLogicFactory.getMaxPlayers(gameName());
    }

    public void start() {
        // TODO: start things with initial state change
    }

    public void userTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        gameLogic.commitTurn(username, pieceId, intendedCoord);

        // TODO: send state change

        if (gameLogic.gameFinishedWinner() != null) {
            // TODO: call comm bridges on both with winner
            // and tell the singleton of winners/losers/draws
            // and destroy this object (from the singleton too)
        }
    }

    public void sendStateChange() {
        // TODO: implement this
        // it will send the board and relevant pool data to each user
        // and it'll also use the diffs list in GameState to send the changes as well
    }

    public int id() { return id; }
    public String pugName() { return pugName; }
    public String gameName() { return gameLogic.gameName(); }
    public List<String> getUsernames() { return usernames; }
    public GameState gameState() { return gameState; }
}
