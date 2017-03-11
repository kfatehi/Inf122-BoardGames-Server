package team5.plugins.test;

import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;

/*
 * @brief   Game Logic for a simple test game
 * @author  Team5
 * @date    2017/03/08
 */
public class TestGameLogic extends GameLogic {
    public TestGameLogic(GameSession session) {
        super(session);
        System.out.println("Creating TestGameLogic");

        gameName = "Test";
    }

    public void initializePieces() {

    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }
}
