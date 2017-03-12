package team5.plugins.tictactoe;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

/*
 * @brief   Game logic for Tic Tac Toe
 * @author  Team5
 * @date    2016/03/08
 */
public class TicTacToeGameLogic extends GameLogic {
    public TicTacToeGameLogic(GameSession session) {
        super(session);
        System.out.println("Created TicTacToeGameLogic");
        gameName = "Tic Tac Toe";
    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(3, 3);
    }

    public boolean needsCheckered() { return false; }
    public boolean needsFlip() { return false; }

    public void initializePieces() {

    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }
}
