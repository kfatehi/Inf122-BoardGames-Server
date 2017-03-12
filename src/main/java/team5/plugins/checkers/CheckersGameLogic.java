package team5.plugins.checkers;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

import java.util.ArrayList;

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

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(8, 8);
    }

    public void initializePieces() {
    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {
        // Assuming two users only
        // Win if no more pieces..
        // TODO need to also check if no moves loses.
        // TODO more kings win if no more moves.
        ArrayList<String> usersList = (ArrayList)session.getUsernames();

        String player1 = usersList.get(0);
        String player2 = usersList.get(1);

        if(session.gameState().getUserPiecePool(player1).getPieceCount() == 0)
            return player2;

        if(session.gameState().getUserPiecePool(player2).getPieceCount() == 0)
            return player1;

        return null;
    }
}

