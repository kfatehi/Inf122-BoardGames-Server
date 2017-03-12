package team5.plugins.chess;

//Internal
import team5.game.state.*;

// Native
import java.util.ArrayList;
import java.util.List;

/*
 * @author  Team5
 * @date    2017/03/09
 */
public class PawnPieceLogic extends PieceLogic {
    private boolean hasMovedYet;
    private boolean justDidADoubleForward;

    public PawnPieceLogic() {
        System.out.println("Building pawn logic");
        hasMovedYet = false;
        justDidADoubleForward = false;
    }

    public boolean justDidADoubleForward() { return justDidADoubleForward; }

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {

        // Piece not on the board, no normal placement behavior
        if (pc.getRow() == -1 || pc.getColumn() == -1) {
            return new ArrayList<PieceCoordinate>();
        }

        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();
        int relativeForward = (pieceRef.getDirection() == MovementDirection.Up ? 1 : -1);

        // Move forward 1
        PieceCoordinate singleForwardPos = new PieceCoordinate(pc.getRow()+relativeForward, pc.getColumn());
        if (b.validCoordinate(singleForwardPos) && b.getPiece(singleForwardPos) == null) {
            coords.add(singleForwardPos);
        }

        // Move forward 2
        PieceCoordinate doubleForwardPos = new PieceCoordinate(pc.getRow()+(2*relativeForward), pc.getColumn());
        if (b.validCoordinate(doubleForwardPos) && b.getPiece(doubleForwardPos) == null && hasMovedYet == false) {
            // also check +1
            coords.add(doubleForwardPos);
        }

        // Captures
        PieceCoordinate captureLeft = new PieceCoordinate(pc.getRow()+relativeForward, pc.getColumn()-1);
        PieceCoordinate captureRight = new PieceCoordinate(pc.getRow()+relativeForward, pc.getColumn()+1);
        for (PieceCoordinate captPC : new PieceCoordinate[]{captureLeft, captureRight}) {
            // Ignore off-board coords
            if (!b.validCoordinate(captPC)) continue;

            if (b.getPiece(captPC) != null) {
                if (b.getPiece(captPC).getUsername().equals(pieceRef.getUsername())) {
                    // There's an Enemy piece in the diagonal capture coord, so we can capture it
                    coords.add(captPC);
                } else {
                    // Our own piece, can't go there. Also this will skip the below En Passant
                }
            } else {
                
                // En Passant
                // If the piece to your side just did a double move forward
                // then you can still move diagonally into an empty space
                // capturing the piece to your side
                // https://en.wikipedia.org/wiki/Pawn_(chess)#Capturing
                Piece sidePiece = b.getPiece(new PieceCoordinate(captPC.getRow()-relativeForward, captPC.getColumn()));
                if (sidePiece != null && PawnPieceLogic.class.isInstance(sidePiece.getPieceLogic())) {
                    PawnPieceLogic sidePiecePawnLogic = (PawnPieceLogic)sidePiece.getPieceLogic();
                    if (sidePiecePawnLogic.justDidADoubleForward()) {
                        coords.add(captPC);
                    }
                }
            }
        }


        return coords;
    }

    // This is a custom function that ChessGameLogic will have to call
    // It is specific to this class, so ChessGameLogic will do casting
    public void movedFromTo(PieceCoordinate coord1, PieceCoordinate coord2) {
        hasMovedYet = true;
        justDidADoubleForward = false;

        if (Math.abs(coord1.getRow() - coord2.getRow()) == 2 && coord1.getColumn() == coord2.getColumn()) {
            // The piece was moved two spaces forward
            justDidADoubleForward = true;
        }
    }

    public String canChangeToPiece() {
        return null;
    }
}
