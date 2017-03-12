package team5.game;

import team5.game.state.GameState;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

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



    public void turnError() {

    }

}
