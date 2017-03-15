package team5.plugins.checkers;

import team5.game.state.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hero on 3/11/17.
 */
public class CheckersPieceLogic extends PieceLogic {

    protected boolean canHop;
    public CheckersPieceLogic() {
        super();
        canHop = false;
    }
    public CheckersPieceLogic(Piece p) {
        pieceRef = p;
        canHop = false;
    }

    @Override
    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();


        moveableCoordinates.addAll(moveableHops(b, pc, pieceRef.getDirection()));
        // Enforce rule: If piece can hop, then it has to do it.
        if(moveableCoordinates.size() > 0) {
            canHop = true;
        } else {
            canHop = false;
        }
        moveableCoordinates.addAll(moveableNoHops(b, pc, pieceRef.getDirection()));

        return moveableCoordinates;
    }

    /*
    @brief  Returns a list of PieceCoordinates that a checker piece can move to without hopping
     */
    public List<PieceCoordinate> moveableNoHops(Board b, PieceCoordinate pc, MovementDirection movement) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();

        // If piece can hop, it must take it
        if(!canHop) {
            int verticalDirInt = 0;
            if (movement == MovementDirection.Up)
                verticalDirInt = 1;
            else
                verticalDirInt = -1;

            // Calculating next possible position
            PieceCoordinate moveOneForwardLeft = new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() - 1),
                    moveOneForwardRight = new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() + 1);

            if (this.coordinateWithinBounds(b, moveOneForwardLeft) && (b.getPiece(moveOneForwardLeft) == null))
                moveableCoordinates.add(moveOneForwardLeft);

            if (this.coordinateWithinBounds(b, moveOneForwardRight) && b.getPiece(moveOneForwardRight) == null)
                moveableCoordinates.add(moveOneForwardRight);
        }
        return moveableCoordinates;
    }

    /*
    @brief  Returns a list of PieceCoordinates that a checker piece can move to with hopping
     */
    public List<PieceCoordinate> moveableHops(Board b, PieceCoordinate pc, MovementDirection movement) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();

        int verticalDirInt = 0;
        if(movement == MovementDirection.Up)
            verticalDirInt = 1;
        else
            verticalDirInt = -1;

        // Calculating next possible position
        PieceCoordinate moveOneForwardLeft =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() - 1),
                moveOneForwardRight =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() +  1);
        // Calculating next possible position
        PieceCoordinate hopOneForwardLeft =  new PieceCoordinate(moveOneForwardLeft.getRow() + verticalDirInt, moveOneForwardLeft.getColumn() - 1),
                hopOneForwardRight =  new PieceCoordinate(moveOneForwardRight.getRow() + verticalDirInt, moveOneForwardRight.getColumn() +  1);

        if(this.coordinateWithinBounds(b, moveOneForwardLeft) &&(b.getPiece(moveOneForwardLeft) != null) &&
                ! b.getPiece(moveOneForwardLeft).getUsername().equals(pieceRef.getUsername())) {
            if(this.coordinateWithinBounds(b, hopOneForwardLeft) && b.getPiece(hopOneForwardLeft) == null)
                moveableCoordinates.add(hopOneForwardLeft);
        }

        if(this.coordinateWithinBounds(b, moveOneForwardRight) && b.getPiece(moveOneForwardRight) != null &&
                ! b.getPiece(moveOneForwardRight).getUsername().equals(pieceRef.getUsername()) ) {
            if(this.coordinateWithinBounds(b, hopOneForwardRight) && b.getPiece(hopOneForwardRight) == null)
                moveableCoordinates.add(hopOneForwardRight);
        }
        return moveableCoordinates;
    }

    public boolean canPieceHop() { return canHop; }

}
