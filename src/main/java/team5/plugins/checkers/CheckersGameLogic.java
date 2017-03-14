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

    // Checker Piece Images
    private Hashtable<String, String> relativeImages = new Hashtable<String, String>() {
        {
            put("Regular_Player1", "/Checkers_Regular_Gorilla.png");
            put("Regular_Player2", "/Checkers_Regular_Banana.png");
            //put("King_Player1", "/Checkers_King_Gorilla.png");
            put("King_Player1", "/Checkers_King_DK.png");
            put("King_Player2", "/Checkers_King_Banana.jpg");
        }
    };

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
//        return new Pair<Integer, Integer>(5, 5);
    }

    public boolean needsCheckered() { return true; }
    public boolean needsFlip(String username) { return !username.equals(player1); }

    public void initializePieces() {
        // Set turn type to be movement of piece
        session.nextTurnType(TurnType.Move);

        // Set users
        player1 = session.getUsernames().get(0);
        player2 = session.getUsernames().get(1);

        // Create Bottom Board pieces -> Gorilla Pieces
        for(int row = 0; row < 3; row++) {
//        for(int row = 0; row < 1; row++) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(player1, relativeImages.get("Regular_Player1"),
                         checkerLogic, MovementDirection.Up);
                checkerLogic.setPieceReference(p);


                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }

        // Create top Board Pieces -> Banana Pieces
        for(int row = getBoardSize().getFirst() - 1; row >= getBoardSize().getFirst() - 3; row--) {
//        for(int row = getBoardSize().getFirst() - 1; row >= getBoardSize().getFirst() - 1; row--) {
            for (int col = row % 2; col < getBoardSize().getSecond(); col += 2) {
                PieceLogic checkerLogic = PieceLogicFactory.createPieceLogic("Checker");
                Piece p = new Piece(player2, relativeImages.get("Regular_Player2"), checkerLogic, MovementDirection.Down);
                checkerLogic.setPieceReference(p);

                session.gameState().newBoardPiece(p, new PieceCoordinate(row, col));
            }
        }
    }

    @Override
    public Map<Piece, List<PieceCoordinate>> getValidMovements(String username) {
        Hashtable<Piece, List<PieceCoordinate>> validMoves = new Hashtable<Piece, List<PieceCoordinate>>();

        Board b = session.gameState().getBoard();
        // Check if player just hopped. If so, only get hoppable moves for that single piece
        if(userCanHopAgain) {
            // If king piece, get hops for any direction else get next hop moving "forward"
            if(pieceToHopAgain.getPieceLogic() instanceof CheckersKingPieceLogic) {

            } else {
                List<PieceCoordinate> pieceNextValidCoordinate = ((CheckerPieceLogic) pieceToHopAgain.getPieceLogic()).moveableHops(b, b.getPiece(pieceToHopAgain.getId()), pieceToHopAgain.getDirection());
                validMoves.put(pieceToHopAgain, pieceNextValidCoordinate);
            }

            return validMoves;
        }

        // If not, then get regular moves
        for(Piece p : b.getAllPieces()) {
            if(p.getUsername().equals(username)) {
                List<PieceCoordinate> legalMoves = b.getLegalMovesOfPiece(p.getId());
                validMoves.put(p, legalMoves);
            }
        }

        return validMoves;
    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
        Board b = session.gameState().getBoard();
        PieceCoordinate pieceCurrentCoordinate = b.getPiece(pieceId);
        Piece p = b.getPiece(pieceCurrentCoordinate);
        GameState gs = session.gameState();

        // Determine if chosen coordinate is hoppable and then check if you can hop again.
//        List<PieceCoordinate> hoppableCoordinates = ((CheckerPieceLogic)p.getPieceLogic()).moveableHops(b, pieceCurrentCoordinate, p.getDirection());
        List<PieceCoordinate> hoppableCoordinates = this.getHoppableBasedOnPieceLogic(p, pieceCurrentCoordinate) ;
        if(hoppableCoordinates.contains(intendedCoord)) {
//            List<PieceCoordinate> nextHoppableCoordinates = ((CheckerPieceLogic)p.getPieceLogic()).moveableHops(b, intendedCoord, p.getDirection());
            List<PieceCoordinate> nextHoppableCoordinates = this.getHoppableBasedOnPieceLogic(p, intendedCoord);

            // User can hop again, so allow hop for this one piece
            if(nextHoppableCoordinates.size() > 0) {
                userCanHopAgain = true;
                pieceToHopAgain = p;
            } else {
                userCanHopAgain = false;
                pieceToHopAgain = null;
            }

            // Get piece hopped over and Capture piece
            PieceCoordinate capturedPC = obtainPieceCoordinateOfCaptured(pieceCurrentCoordinate, intendedCoord) ;

            if(capturedPC == null) {
                System.out.println("ERROR:CommitTurn>>>Could Not find piece hopped over");
            } else {
                // Safety Precaution
                gs.capturePiece(b.getPiece(capturedPC).getId());
            }

        }

        // Check for phase change :) DK COMING IN HOT

        if(!userCanHopAgain) {
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
        rowOffset = rowOffset / Math.abs(rowOffset);
        colOffset = colOffset / Math.abs(colOffset);

        GameState gs = session.gameState();
        // For loop until enemy piece coordinate found
        for(int row = startPC.getRow() + rowOffset; row != endPC.getRow(); row += rowOffset) {
            for(int col = startPC.getColumn() + colOffset; col != endPC.getColumn(); col += colOffset) {
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
        ArrayList<Piece> allPiecesOnBoard = session.gameState().getBoard().getAllPieces();
        int player1PieceCount = 0,
            player2PieceCount = 0;

        for(Piece p : allPiecesOnBoard) {
            if(p.getUsername().equals(player1))
                player1PieceCount += 1;
            if(p.getUsername().equals(player2))
                player2PieceCount += 1;
        }

//        System.out.println("Player 1 count: " + String.valueOf(player1PieceCount));
//        System.out.println("Player 2 count: " + String.valueOf(player2PieceCount));
        // Win Condition 1: No more pieces for a player
        if(player1PieceCount == 0)
            return player2;
        if(player2PieceCount == 0)
            return player1;

        // Win Condition 2: If a player cannot make anymore moves and its their turn
        //      - Check who has more kings wins.


        return null;
    }

    private List<PieceCoordinate> getHoppableBasedOnPieceLogic(Piece p, PieceCoordinate pieceCurrentCoordinate) {
        Board b = session.gameState().getBoard();
        PieceLogic pl = p.getPieceLogic();

        List<PieceCoordinate> pcList = new ArrayList<>();
        // King
        if(pl instanceof CheckersKingPieceLogic) {
            pcList.addAll(((CheckerPieceLogic)pl).moveableHops(b, pieceCurrentCoordinate, MovementDirection.Down));
            pcList.addAll(((CheckerPieceLogic)pl).moveableHops(b, pieceCurrentCoordinate, MovementDirection.Up));

        }
        // Regular
        else {
            pcList.addAll(((CheckerPieceLogic)pl).moveableHops(b, pieceCurrentCoordinate, p.getDirection()));
        }

        return pcList;
    }
}

