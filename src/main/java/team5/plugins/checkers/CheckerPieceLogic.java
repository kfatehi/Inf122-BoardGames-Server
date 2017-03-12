package team5.plugins.checkers;

import team5.game.state.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hero on 3/11/17.
 */
public class CheckerPieceLogic extends PieceLogic {

    public CheckerPieceLogic() {}
    public CheckerPieceLogic(Piece p) {
        pieceRef = p;
    }

    @Override
    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();

        moveableCoordinates.addAll(moveableForwardNoHops(b, pc));
        moveableCoordinates.addAll(moveableForwardHops(b, pc));

        return moveableCoordinates;
    }

    /*
    @brief  Returns a list of PieceCoordinates that a checker piece can move to without hopping
     */
    public List<PieceCoordinate> moveableForwardNoHops(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();

        int verticalDirInt = 0;
        if(pieceRef.getDirection() == MovementDirection.Up)
            verticalDirInt = 1;
        else
            verticalDirInt = -1;

        // Calculating next possible position
        PieceCoordinate moveOneForwardLeft =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() - 1),
                moveOneForwardRight =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() +  1);

        if(this.coordinateWithinBounds(b, moveOneForwardLeft) &&(b.getPiece(moveOneForwardLeft) == null) )
            moveableCoordinates.add(moveOneForwardLeft);

        if(this.coordinateWithinBounds(b, moveOneForwardRight) && b.getPiece(moveOneForwardRight) == null)
            moveableCoordinates.add(moveOneForwardRight);

        return moveableCoordinates;
    }

    /*
    @brief  Returns a list of PieceCoordinates that a checker piece can move to with hopping
     */
    public List<PieceCoordinate> moveableForwardHops(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> moveableCoordinates = new ArrayList<PieceCoordinate>();

        int verticalDirInt = 0;
        if(pieceRef.getDirection() == MovementDirection.Up)
            verticalDirInt = 1;
        else
            verticalDirInt = -1;

        // Calculating next possible position
        PieceCoordinate moveOneForwardLeft =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() - 1),
                moveOneForwardRight =  new PieceCoordinate(pc.getRow() + verticalDirInt, pc.getColumn() +  1);
        // Calculating next possible position
        PieceCoordinate hopOneForwardLeft =  new PieceCoordinate(moveOneForwardLeft.getRow() + verticalDirInt, moveOneForwardLeft.getColumn() - 1),
                hopOneForwardRight =  new PieceCoordinate(moveOneForwardRight.getRow() + verticalDirInt, moveOneForwardRight.getColumn() +  1);

        if(this.coordinateWithinBounds(b, moveOneForwardLeft) &&(b.getPiece(moveOneForwardLeft) != null) ) {
            if(this.coordinateWithinBounds(b, hopOneForwardLeft) && b.getPiece(hopOneForwardLeft) == null)
                moveableCoordinates.add(hopOneForwardLeft);
        }

        if(this.coordinateWithinBounds(b, moveOneForwardRight) && b.getPiece(moveOneForwardRight) != null) {
            if(this.coordinateWithinBounds(b, hopOneForwardRight) && b.getPiece(hopOneForwardRight) == null)
                moveableCoordinates.add(hopOneForwardRight);
        }

        return moveableCoordinates;
    }

    @Override
    public String canChangeToPiece() {
        return null;
    }
}
