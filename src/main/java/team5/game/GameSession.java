package team5.game;

import javafx.util.Pair;
import team5.game.state.GameState;
import team5.game.state.PieceCoordinate;
import team5.network.CommunicationBridge;
import team5.plugins.chess.ChessGameLogic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import team5.game.GameLogicFactory;

/**
 * Created by james on 3/7/17.
 */
public class GameSession {

    private static int nextId = 0;

    private int id;
    private String pugName;
    private String currentUserTurn;

    private List<String> usernames = new ArrayList<String>();
    private Map<String, CommunicationBridge> commBridges = new HashMap<String, CommunicationBridge>();
    private GameState gameState;
    private GameLogic gameLogic;

    public GameSession(){
        id = nextId;
        nextId += 1;
    }
    public GameSession(String gameName, String pugName, String currentUser) {
        System.out.println("Building GameSession");

        id = nextId;
        nextId += 1;

        this.pugName = pugName;
        this.currentUserTurn = currentUser;

        GameLogicFactory gameLogicFactory = new GameLogicFactory();
        gameLogic = gameLogicFactory.createGameLogic(gameName, this);

        Pair<Integer, Integer> size = gameLogic.getBoardSize();
        gameState = new GameState(size.getKey(), size.getValue());
    }

    public void updateBridge(String username, CommunicationBridge bridge) {
        commBridges.put(username, bridge);
    }
    public void switchTurn(String username){
    	if(usernames.contains(username)){
    		currentUserTurn = username;
    	}
    	else System.out.println("INVALID USERNAME");
    }
    public void addUser(String username, CommunicationBridge commBridge) {
        usernames.add(username);
        commBridges.put(username, commBridge);
    }

    public boolean isFull() {
        return usernames.size() >= GameLogicFactory.getMaxPlayers(gameName());
    }

    public void start() {

        // Prepare the game logic now that all players are in
        gameLogic.initializePieces();

        // Send GAME_INIT to all players
        usernames.forEach(user->{
            // All users except the one we're sending it to
            String[] opponents = usernames.stream().filter(u->!u.equals(user)).toArray(String[]::new);
            CommunicationBridge cbp = commBridges.get(user);
            if (cbp != null) {
                cbp.sendGameStart(id, opponents, false, true, 3, 3);
            }
        });

        // And then send our initial state change for the players
        sendStateChange();
    }

    public void userTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        // Give the turn to the game logic which will make changes to the state
        // and also the next player, etc.
        gameLogic.commitTurn(username, pieceId, intendedCoord);

        // Send the corresponding state change
        sendStateChange();


        String winner = gameLogic.gameFinishedWinner();
        if (winner != null) {
            usernames.forEach(user->{
                CommunicationBridge commBridge = commBridges.get(user);
                if (commBridge != null) {
                    // TODO: Send GAME_END
                }
            });

            // Update the singleton
            if (winner.equals("")) {
                usernames.forEach(user-> GameManagerSingleton.instance().userDraw(user, gameName()));
            } else {
                GameManagerSingleton.instance().userWon(winner, gameName());
                usernames.stream().
                        filter(u->!u.equals(winner)).
                        forEach(user->GameManagerSingleton.instance().userLost(user, gameName()));
            }

        }
    }

    public void sendStateChange() {
        // TODO: implement this
        // it will send the board and relevant pool data to each user
        // and it'll also use the diffs list in GameState to send the changes as well
        usernames.forEach(user->{
            CommunicationBridge cbp = commBridges.get(user);
            if (cbp != null) {
                cbp.sendGameStateChange();
            }
        });

    }

    public int id() { return id; }
    public String pugName() { return pugName; }
    public String gameName() { return gameLogic.gameName(); }
    public List<String> getUsernames() { return usernames; }
    public GameState gameState() { return gameState; }
}
