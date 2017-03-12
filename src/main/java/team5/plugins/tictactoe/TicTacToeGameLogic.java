package team5.plugins.tictactoe;

// Internal
import javafx.util.Pair;
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;

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
        return new Pair<Integer, Integer>(5, 5);
    }

    public void initializePieces() {

    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }
}
