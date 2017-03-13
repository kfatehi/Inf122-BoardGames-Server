package team5.plugins.checkers;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.TurnType;
import team5.game.state.*;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

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

    public boolean needsCheckered() { return true; }
    public boolean needsFlip() { return true; }

    public void initializePieces() {
        // Set turn type to be movement of piece
        session.nextTurnType(TurnType.Move);

        // Create red pieces
        ArrayList<String> usersList = (ArrayList)session.getUsernames();
        for(int row = 0; row < 3; row++) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(usersList.get(0), "http://cdnjs.cloudflare.com/ajax/libs/twemoji/2.2.0/2/svg/1f98d.svg",
                         checkerLogic, MovementDirection.Up);
                checkerLogic.setPieceReference(p);


                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Create black pieces
        for(int row = getBoardSize().getFirst() - 1; row >= getBoardSize().getFirst() - 3; row--) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(usersList.get(1), "http://cdnjs.cloudflare.com/ajax/libs/emojione/2.2.6/assets/svg/1f34c.svg", checkerLogic, MovementDirection.Down);
                checkerLogic.setPieceReference(p);

                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }
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

