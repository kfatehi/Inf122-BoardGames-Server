package team5.plugins.checkers;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.TurnType;
import team5.game.state.*;
import team5.util.Pair;

import java.lang.reflect.Array;
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

    private String  player1,
                    player2;
    // Allows for multihopping
    private boolean userCanHopAgain;
    private Piece pieceToHopAgain;

    public CheckersGameLogic(GameSession session) {
        super(session);
        System.out.println("Created CheckersGameLogic");

        gameName = "Checkers";
        userCanHopAgain = false;
        pieceToHopAgain = null;

    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(8, 8);
    }

    public boolean needsCheckered() { return true; }
    public boolean needsFlip() { return true; }

    public void initializePieces() {
        // Set turn type to be movement of piece
        session.nextTurnType(TurnType.Move);

        // Set users
        player1 = session.getUsernames().get(0);
        player2 = session.getUsernames().get(1);

        // Create Bottom Board pieces -> Gorilla Pieces
        for(int row = 0; row < 3; row++) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(player1, "http://cdnjs.cloudflare.com/ajax/libs/twemoji/2.2.0/2/svg/1f98d.svg",
                         checkerLogic, MovementDirection.Up);
                checkerLogic.setPieceReference(p);


                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Create top Board Pieces -> Banana Pieces
        for(int row = getBoardSize().getFirst() - 1; row >= getBoardSize().getFirst() - 3; row--) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(player2, "http://cdnjs.cloudflare.com/ajax/libs/emojione/2.2.6/assets/svg/1f34c.svg", checkerLogic, MovementDirection.Down);
                checkerLogic.setPieceReference(p);

                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }
    }

    @Override
    public Map<Piece, List<PieceCoordinate>> getValidMovements(String username) {
        Hashtable<Piece, List<PieceCoordinate>> validMoves = new Hashtable<Piece, List<PieceCoordinate>>();

        // Check if player just hopped. If so, only get hoppable moves for that single piece
        if(userCanHopAgain) {

        }

        // If not, then get regular moves
        Board b = session.gameState().getBoard();
        for(Piece p : b.getAllPieces()) {
            List<PieceCoordinate> legalMoves = b.getLegalMovesOfPiece(p.getId());
            validMoves.put(p, legalMoves);
        }

        return validMoves;
    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        Board b = session.gameState().getBoard();
        PieceCoordinate pieceCurrentCoordinate = b.getPiece(pieceId);
        Piece p = b.getPiece(pieceCurrentCoordinate);
        GameState gs = session.gameState();

        // Assuming user piece is "legal"

        // Determine if chosen coordinate is hoppable and then check if you can hop again.
        List<PieceCoordinate> hoppableCoordinates = ((CheckerPieceLogic)p.getPieceLogic()).moveableForwardHops(b, pieceCurrentCoordinate);
        if(hoppableCoordinates.contains(intendedCoord)) {
            List<PieceCoordinate> nextHoppableCoordinates = ((CheckerPieceLogic)p.getPieceLogic()).moveableForwardHops(b, pieceCurrentCoordinate);

            // User can hop again, so allow hop for this one piece
            if(nextHoppableCoordinates.size() > 0) {
                userCanHopAgain = true;
                pieceToHopAgain = p;
            }

            // Get piece hopped over and Capture piece
            PieceCoordinate capturedPC = obtainPieceCoordinateOfCaptured(pieceCurrentCoordinate, intendedCoord) ;

            if(capturedPC == null) {
                System.out.println("ERROR:CommitTurn>>>Could Not find piece hopped over");
            }

            gs.capturePiece(b.getPiece(capturedPC).getId());
        } else {
            session.switchTurn(switchUsers(session.getCurrentUserTurn()));
        }

        // Move Piece to new location
        gs.movePiece(pieceId, intendedCoord);
    }

    private String switchUsers(String username) {
        if(username.equals(player1))
            return player2;

        return player1;
    }
    private PieceCoordinate obtainPieceCoordinateOfCaptured(PieceCoordinate startPC, PieceCoordinate endPC) {

        // Get piece hopped over and Capture piece
        int rowOffset = endPC.getRow() - startPC.getRow();
        int colOffset = endPC.getColumn() - startPC.getColumn();

        // Make sure that they are +1 or -1
        rowOffset = rowOffset / rowOffset;
        colOffset = colOffset / colOffset;

        GameState gs = session.gameState();
        // For loop until enemy piece coordinate found
        for(int row = startPC.getRow(); row < endPC.getRow(); row += rowOffset) {
            for(int col = startPC.getColumn(); col < endPC.getColumn(); col += colOffset) {
                PieceCoordinate calculatedPC = new PieceCoordinate(row, col);

                if(gs.pieceExistsAt(calculatedPC))
                    return calculatedPC;
            }
        }

        return null;

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

