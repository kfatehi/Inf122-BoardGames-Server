package team5.plugins.chess;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.state.MovementDirection;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import team5.util.Pair;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private String whitePlayer;
    private String blackPlayer;

    public ChessGameLogic(GameSession session) {
        super(session);
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(ROWS, COLS);
    }

    public boolean needsCheckered() { return true; }
    public boolean needsFlip() { return true; }

    public void initializePieces() {
        List<String> pieceNames = Arrays.asList("Rook", "Knight", "Bishop", "Queen", "King", "Bishop", "Knight", "Rook");

        whitePlayer = session.getUsernames().get(0);
        blackPlayer = session.getUsernames().get(1);

        // Main pieces
        for (int row : Arrays.asList(0,7)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic(pieceNames.get(row)));
                p.setImage(pieceNames.get(row));
                p.setDirection(row == 0 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(row == 0 ? whitePlayer : blackPlayer);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Pawns
        for (int row : Arrays.asList(1,6)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic("Pawn"));
                p.setImage("Pawn");
                p.setDirection(row == 1 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(row == 1 ? whitePlayer : blackPlayer);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }


    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {

    }

    public  String gameFinishedWinner() {

        return null;
    }

}
