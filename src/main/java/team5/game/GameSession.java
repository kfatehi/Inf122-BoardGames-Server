package team5.game;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import team5.game.state.GameState;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.network.CommunicationBridge;
import team5.plugins.chess.ChessGameLogic;

import java.util.*;

import team5.game.GameLogicFactory;
import team5.util.Pair;

/**
 * Created by james on 3/7/17.
 */
public class GameSession {

    private static int nextId = 0;

    private int id;
    private String pugName;
    private String currentUserTurn;
    private TurnType currentTurnType;

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
        this.currentTurnType = TurnType.Place;

        GameLogicFactory gameLogicFactory = new GameLogicFactory();
        gameLogic = gameLogicFactory.createGameLogic(gameName, this);

        Pair<Integer, Integer> size = gameLogic.getBoardSize();
        gameState = new GameState(size.getFirst(), size.getSecond());
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
    public void nextTurnType(TurnType type) {
        currentTurnType = type;
    }
    public void addUser(String username, CommunicationBridge commBridge) {
        usernames.add(username);
        commBridges.put(username, commBridge);
        gameState.newUser(username);
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
                Pair<Integer, Integer> size = gameLogic.getBoardSize();
                cbp.sendGameStart(id, opponents, gameLogic.needsFlip(user), gameLogic.needsCheckered(), size.getFirst(), size.getSecond());
            }
        });

        // And then send our initial state change for the players
        sendStateChange();
    }

    public void userRejoined(String username) {
        CommunicationBridge commBridge = commBridges.get(username);
        String[] opponents = usernames.stream().filter(u->!u.equals(username)).toArray(String[]::new);

        if (commBridge != null) {
            Pair<Integer, Integer> size = gameLogic.getBoardSize();
            commBridge.sendGameStart(id, opponents, gameLogic.needsFlip(username), gameLogic.needsCheckered(), size.getFirst(), size.getSecond());
        }

        sendStateChangeFor(username);
    }

    public void userTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        // Ignore turns from the wrong player
        if (!username.equals(currentUserTurn)) {
            return;
        }

        // Give the turn to the game logic which will make changes to the state
        // and also the next player, etc.
    	gameState.resetDiffs();
        gameLogic.commitTurn(username, pieceId, intendedCoord);

        // Send the corresponding state change
        sendStateChange();


        String winner = gameLogic.gameFinishedWinner();
        if (winner != null) {
            usernames.forEach(user->{
                CommunicationBridge commBridge = commBridges.get(user);
                if (commBridge != null) {
                    commBridge.sendGameEnd(winner);
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

            // Delete the game
            GameManagerSingleton.instance().destroyGameSession(this);
        }
    }

    private void sendStateChange() {

        usernames.forEach(this::sendStateChangeFor);
    }

    private void sendStateChangeFor(String user) {
        CommunicationBridge cbp = commBridges.get(user);
        if (cbp != null) {
            List<PieceCoordinate> validPlacements = new ArrayList<PieceCoordinate>();
            Map<Piece, List<PieceCoordinate>> validMovements = new HashMap<Piece, List<PieceCoordinate>>();
            // Only send (and calc) valid moves for whose turn it is
            if (user.equals(currentUserTurn)) {
                validPlacements = gameLogic.getValidPlacements(user);
                validMovements = gameLogic.getValidMovements(user);
            }

            cbp.sendStateChange(currentUserTurn, currentTurnType.toString(), gameState.getBoard(),
                    gameState.getUserPiecePool(user), validPlacements, validMovements, gameState.getDiffs());
        }
    }

    public int id() { return id; }
    public String pugName() { return pugName; }
    public String gameName() { return gameLogic.gameName(); }
    public List<String> getUsernames() { return usernames; }
    public GameState gameState() { return gameState; }
    public String getCurrentUserTurn() { return currentUserTurn; }
}
