package team5.plugins.chess;

// Internal
import team5.game.GameLogic;
import team5.game.state.PieceCoordinate;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    public ChessGameLogic() {
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
    }

    public void initializePieces() {

    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }

}
