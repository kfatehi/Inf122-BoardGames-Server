package team5.plugins.chess;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.TurnType;
import team5.game.state.*;

import java.util.*;

import team5.util.Pair;

/*
 * @brief   Game logic for chess
 * @author  Team5
 * @date    2017/03/08
 */
public class ChessGameLogic extends GameLogic {

    private static final int ROWS = 8;
    private static final int COLS = 8;

    private static Map<String, Pair<String, String>> images = new HashMap<String, Pair<String, String>>() {{
        put("Rook",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_rlt.png", "http://jamamp.ddns.net/inf122icons/Chess_rdt.png"));
        put("Knight", new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_klt.png", "http://jamamp.ddns.net/inf122icons/Chess_kdt.png"));
        put("Bishop", new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_blt.png", "http://jamamp.ddns.net/inf122icons/Chess_bdt.png"));
        put("Queen",  new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_qlt.png", "http://jamamp.ddns.net/inf122icons/Chess_qdt.png"));
        put("King",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_klt.png", "http://jamamp.ddns.net/inf122icons/Chess_kdt.png"));
        put("Pawn",   new Pair<>("http://jamamp.ddns.net/inf122icons/Chess_plt.png", "http://jamamp.ddns.net/inf122icons/Chess_pdt.png"));
    }};

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

        session.nextTurnType(TurnType.Move);

        // Main pieces
        for (int row : Arrays.asList(0,7)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic(pieceNames.get(col)));
                p.getPieceLogic().setPieceRef(p);
                String u = row == 0 ? whitePlayer : blackPlayer;
                p.setImage(imageFor(pieceNames.get(col), u));
                p.setDirection(row == 0 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(u);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Pawns
        for (int row : Arrays.asList(1,6)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic("Pawn"));
                p.getPieceLogic().setPieceRef(p);
                String u = row == 1 ? whitePlayer : blackPlayer;
                p.setImage(imageFor("Pawn", u));
                p.setDirection(row == 1 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(u);
                state().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }


    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        Board board = state().getBoard();
        PieceCoordinate current = board.getPiece(pieceId);
        Piece piece = board.getPiece(current);

        // First check for the move's validity
        boolean validMove = false;
        for (PieceCoordinate moveableCoord : piece.getPieceLogic().moveableCoordinates(board, current)) {
            if (intendedCoord.getRow() == moveableCoord.getRow() && intendedCoord.getColumn() == moveableCoord.getColumn()) {
                validMove = true;
            }
        }
        if (!validMove) {
            System.out.println("Unexpected error, client moving piece to a place that's not allowed");
            return;
        }

        // Commit it to the state
        state().movePiece(pieceId, intendedCoord);

        // Pawn-specific
        Piece pawnPiece = state().getPieceAt(intendedCoord);
        if (PawnPieceLogic.class.isInstance(piece.getPieceLogic())) {

            // Is a Pawn, need to notify it so it knows its first move and such
            PawnPieceLogic pawnPL = (PawnPieceLogic)piece.getPieceLogic();
            pawnPL.movedFromTo(current, intendedCoord);

            // Pawn Promotion
            if ((intendedCoord.getRow() == 0 && pawnPiece.getUsername().equals(blackPlayer)) ||
                    (intendedCoord.getRow() == ROWS-1 && pawnPiece.getUsername().equals(whitePlayer))) {
                // This piece that was just moved is a pawn
                // and it was moved by the player to the other side of the board

                // TODO: Send a dialog to the same user asking to promote their pawn
                // can select from: Rook, Bishop, Knight, Queen (no pawn, no king)

                // And we won't let the turn change to the other player
                // or do other calcs since this is still the player's turn
                //return;
            }
        }


        // Change the turn
        if (username.equals(whitePlayer)) {
            session.switchTurn(blackPlayer);
        } else {
            session.switchTurn(whitePlayer);
        }
    }

    public  String gameFinishedWinner() {

        return null;
    }

    private boolean inCheck() {
        return false;
    }

    private String imageFor(String pieceName, String username) {
        Pair<String, String> pair = images.get(pieceName);
        if (username.equals(whitePlayer)) {
            return pair.getFirst();
        } else {
            return pair.getSecond();
        }
    }

}
