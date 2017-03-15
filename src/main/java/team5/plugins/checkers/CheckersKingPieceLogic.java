package team5.plugins.checkers;

import team5.game.state.*;

import java.util.ArrayList;
import java.util.List;
/**
 * Created by hero on 3/13/17.
 */
public class CheckersKingPieceLogic extends CheckersPieceLogic {

    public CheckersKingPieceLogic() {
        super();
    }
    public CheckersKingPieceLogic(Piece p) {
        super(p);
    }

    @Override
    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> validMove = new ArrayList<>();


        // Get all moves with hopping
        validMove.addAll(this.moveableHops(b, pc, MovementDirection.Down));
        validMove.addAll(this.moveableHops(b, pc, MovementDirection.Up));

        // Enforce rule: If piece can hop, then it has to do it.
        if(validMove.size() > 0) {
            canHop = true;
        } else {
            canHop = false;
        }

        // Get all moves without hopping
        validMove.addAll(this.moveableNoHops(b, pc, MovementDirection.Down));
        validMove.addAll(this.moveableNoHops(b, pc, MovementDirection.Up));


        return validMove;
    }


}
