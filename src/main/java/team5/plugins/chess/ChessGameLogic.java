package team5.plugins.chess;

// Internal
import javafx.util.Pair;
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    public ChessGameLogic(GameSession session) {
        super(session);
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
    }

    public void initializePieces() {
        List<String> pieceNames = Arrays.asList("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook");

        PieceLogicFactory fact = new PieceLogicFactory();
        // Player 1 main pieces
        int row = 0;
        for (int col = 0; col < COLS; col++) {
            Piece p = new Piece();
//          p.setPieceLogic(fact.createLogic(pieceNames.get(row)));
            state().newBoardPiece(p, new PieceCoordinate(row, col));
        }
        // Player 2 main pieces
        row = 7;
        for (int col = COLS-1; col >= 0; col--) {
            Piece p = new Piece();
//          p.setPieceLogic(fact.createLogic(pieceNames.get(col)));
            state().newBoardPiece(p, new PieceCoordinate(row, col));
        }

        // Pawns
        for (int aRow : Arrays.asList(1,6)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
//                p.setPieceLogic(fact.createLogic(pieceNames.get("Pawn")));
                state().newBoardPiece(p, new PieceCoordinate(aRow, col));
            }
        }


    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }

}
