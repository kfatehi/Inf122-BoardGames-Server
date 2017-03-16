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
        put("Rook",   new Pair<>("/Chess_rlt.png", "/Chess_rdt.png"));
        put("Knight", new Pair<>("/Chess_nlt.png", "/Chess_ndt.png"));
        put("Bishop", new Pair<>("/Chess_blt.png", "/Chess_bdt.png"));
        put("Queen",  new Pair<>("/Chess_qlt.png", "/Chess_qdt.png"));
        put("King",   new Pair<>("/Chess_klt.png", "/Chess_kdt.png"));
        put("Pawn",   new Pair<>("/Chess_plt.png", "/Chess_pdt.png"));
    }};

    private String whitePlayer;
    private String blackPlayer;

    private int whiteKingId;
    private int blackKingId;

    public ChessGameLogic(GameSession session) {
        super(session);
        System.out.println("Created ChessGameLogic");

        gameName = "Chess";
    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(ROWS, COLS);
    }

    public boolean needsCheckered() { return true; }
    public boolean needsFlip(String username) { return !username.equals(whitePlayer); }

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
                p.getPieceLogic().setPieceReference(p);
                String u = row == 0 ? whitePlayer : blackPlayer;
                p.setImage(imageFor(pieceNames.get(col), u));
                p.setDirection(row == 0 ? MovementDirection.Up : MovementDirection.Down);
                p.setUsername(u);
                state().newBoardPiece(p, new PieceCoordinate(row, col));

                // Store the ids of the Kings for later lookup
                if (pieceNames.get(col).equals("King")) {
                    if (u.equals(whitePlayer)) {
                        whiteKingId = p.getId();
                    } else {
                        blackKingId = p.getId();
                    }
                }
            }
        }

        // Pawns
        for (int row : Arrays.asList(1,6)) {
            for (int col = 0; col < COLS; col++) {
                Piece p = new Piece();
                p.setPieceLogic(PieceLogicFactory.createPieceLogic("Pawn"));
                p.getPieceLogic().setPieceReference(p);
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
            if (intendedCoord.equals(moveableCoord)) {
                validMove = true;
            }
        }
        if (!validMove) {
            System.out.println("Unexpected error, client moving piece to a place that's not allowed");
            return;
        }

        // Commit it to the state
        state().movePiece(pieceId, intendedCoord);


        // Now for special cases

        // First most special case things require them to know if they've been moved or not
        // and when we tell them they moved they can give back information
        Piece firstMovePiece = state().getPieceAt(intendedCoord);
        Piece returnedPiece = null;
        if (FirstMovePieceLogic.class.isInstance(firstMovePiece.getPieceLogic())) {
            FirstMovePieceLogic firstMovePieceLogic = (FirstMovePieceLogic)firstMovePiece.getPieceLogic();
            returnedPiece = firstMovePieceLogic.movedFromTo(current, intendedCoord);
        }

        // Pawn-specific
        if (PawnPieceLogic.class.isInstance(piece.getPieceLogic())) {

            // Is a Pawn, need to notify it so it knows its first move and such
            PawnPieceLogic pawnPL = (PawnPieceLogic)piece.getPieceLogic();
            Piece enPassantCapturedPiece = returnedPiece;

            if (enPassantCapturedPiece != null) {
                // Moving that Pawn caused an En Passant capture. The piece coordinate returned is the one it captured
                state().capturePiece(enPassantCapturedPiece.getId());
            }

            // Pawn Promotion
            if ((intendedCoord.getRow() == 0 && piece.getUsername().equals(blackPlayer)) ||
                    (intendedCoord.getRow() == ROWS-1 && piece.getUsername().equals(whitePlayer))) {
                // This piece that was just moved is a pawn
                // and it was moved by the player to the other side of the board

                // TODO: Send a dialog to the same user asking to promote their pawn
                // can select from: Rook, Bishop, Knight, Queen (no pawn, no king)

                // And we won't let the turn change to the other player
                // or do other calcs since this is still the player's turn
                //return;
            }
        } else if (KingPieceLogic.class.isInstance(piece.getPieceLogic())) {
            KingPieceLogic kingPieceLogic = (KingPieceLogic)piece.getPieceLogic();
            Piece castledRook = returnedPiece;
            System.out.println("Returned piece = " + castledRook);

            if (castledRook != null) {
                System.out.println("-> " + kingPieceLogic.rookPos(intendedCoord));
                state().movePiece(castledRook.getId(), kingPieceLogic.rookPos(intendedCoord));
            }
        }


        // Change the turn
        if (username.equals(whitePlayer)) {
            session.switchTurn(blackPlayer);
        } else {
            session.switchTurn(whitePlayer);
        }
    }

    public String gameFinishedWinner() {

        return null;
    }

    @Override
    public Map<Piece, List<PieceCoordinate>> getValidMovements(String username) {
        Map<Piece, List<PieceCoordinate>> map = new HashMap<Piece, List<PieceCoordinate>>();

        // For now, simply add everything
        for (Piece piece : state().getBoard().getAllPieces()) {
            if (piece.getUsername().equals(username)) {
                List<PieceCoordinate> moves = piece.getPieceLogic().moveableCoordinates(state().getBoard(), state().getBoard().getPiece(piece.getId()));
                map.put(piece, moves);
            }
        }

        
        // This logic needs to happen regardless of if the King is in check
        // If the King is in check, then this gives only the moves that places the King out of check in some way
        // But if the King is not in check, it gives only legal moves that don't put the King in check.

        /* Plan of attack:
         * - The King is in a position such that an enemy piece can capture it.
         * - If the line of sight of capture is not broken then the King can
         * be captured resulting in a loss.
         * - The line of sight can be disrupted by the King moving, OR
         * by any other friendly piece moving in the way.
         *
         * 1. Go through all possible valid movements of all pieces
         * 2. For each of those try simulating it and seeing if it removes the check
         *   - If so, that's a valid move.
         *   - If not, they're not allowed to do that as it sacrifices the King
         */

        Map<Piece, List<PieceCoordinate>> validCheckMoves = new HashMap<Piece, List<PieceCoordinate>>();
        map.forEach((piece, moves) -> {
            moves.forEach(move -> {
                if (!simulateMoveForCheck(username, piece.getId(), move)) {
                    // If the simulation resulted in no more check, add the move
                    if (!validCheckMoves.containsKey(piece)) {
                        validCheckMoves.put(piece, new ArrayList<PieceCoordinate>());
                    }
                    validCheckMoves.get(piece).add(move);
                }
            });
        });
        return validCheckMoves;
    }

    private boolean inCheck(String username) {
        PieceCoordinate kingCoord;
        if (username.equals(whitePlayer)) {
            kingCoord = state().getBoard().getPiece(whiteKingId);
        } else {
            kingCoord = state().getBoard().getPiece(blackKingId);
        }
        Piece king = state().getBoard().getPiece(kingCoord);

        for (Piece p : state().getBoard().getAllPieces()) {
            if (p.getUsername().equals(session.getCurrentUserTurn())) {
                // Dont check our own pieces, only enemy
                continue;
            }

            // Check all the places enemy pieces can move to
            List<PieceCoordinate> moveableCoords = p.getPieceLogic().moveableCoordinates(state().getBoard(), state().getBoard().getPiece(p.getId()));
            for (PieceCoordinate moveableCoord : moveableCoords) {
                if (moveableCoord.equals(kingCoord)) {
                    // And if they can move onto and capture our King, we're in check

                    return true;
                }
            }
        }

        return false;
    }

    private boolean simulateMoveForCheck(String username, int pieceId, PieceCoordinate testCoord) {
        // Simulates the movement of a piece to see if it still results in inCheck being true or not.

        // Temporarily disable diffs, as we're making only temp changes to the board
        state().disableDiffs();

        PieceCoordinate oldCoord = state().getBoard().getPiece(pieceId);

        // Make the move
        Piece capturedPiece = state().movePiece(pieceId, testCoord);

        // Sample the function
        boolean isStillInCheck = inCheck(username);

        // Undo the movement
        state().movePiece(pieceId, oldCoord);
        // Re-add any captured pieces
        if (capturedPiece != null) {
            state().movePieceToBoard(capturedPiece.getId(), testCoord);
        }

        // Undo diff disable
        state().enableDiffs();

        return isStillInCheck;
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
