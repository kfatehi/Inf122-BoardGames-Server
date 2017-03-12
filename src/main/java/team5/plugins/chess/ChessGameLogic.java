package team5.plugins.chess;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    public ChessGameLogic(GameSession session) {
        super(session);
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
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
