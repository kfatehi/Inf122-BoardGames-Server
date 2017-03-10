package team5.game.state;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 3/8/17.
 */
public class GameState {

    private Board board;
    private PiecePool serverPool = new PiecePool();
    private PiecePool capturedPool = new PiecePool();
    private Map<String, PiecePool> userPools = new HashMap<String, PiecePool>();

    public GameState(int rows, int cols) {
        board = new Board(rows, cols);
    }

    public void movePieceToBoard(int id) {

    }

    public void movePieceToUserPool(int id, String username) {

    }

    public void capturePiece(int id) {

    }

}
