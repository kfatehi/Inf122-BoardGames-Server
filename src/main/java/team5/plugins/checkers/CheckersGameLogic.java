package team5.plugins.checkers;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;

/*
 * @brief   Game logic for checkers
 * @author  Team5
 * @date    2016/03/08
 */
public class CheckersGameLogic extends GameLogic {
    public CheckersGameLogic(GameSession session) {
        super(session);
        System.out.println("Created CheckersGameLogic");

        gameName = "Checkers";
    }

    public void initializePieces() {

    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }
}

