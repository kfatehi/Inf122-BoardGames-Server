package team5.game;

import team5.game.state.GameState;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/*
 * Abstract class that holds the basic things that every game logic 
 * should handle to allow for new "pluggable" game logics.
 *
 * @author  Team5
 * @date    2017/03/08
 */
public abstract class GameLogic {

    protected String gameName = "";
    protected GameSession session;

    public GameLogic(GameSession session) { this.session = session; }
    protected GameState state() { return session.gameState(); }

    public static void testDriverMethod() {
        System.out.println("Abstract Game Logic working");
    }

    public String gameName() { return gameName; }
    public abstract Pair<Integer,Integer> getBoardSize();
    public abstract boolean needsFlip();
    public abstract boolean needsCheckered();
    public abstract void initializePieces();
    public abstract void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord);
    public abstract String gameFinishedWinner();

    // These two methods return empty valid moves. You must override for filtering.
    public List<PieceCoordinate> getValidPlacements(String username) {
        return new ArrayList<PieceCoordinate>();
    }

    public Map<Piece, List<PieceCoordinate>> getValidMovements(String username) {
        return new Hashtable<Piece, List<PieceCoordinate>>();
    }



    public void turnError() {

    }

}
