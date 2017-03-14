package team5.game.state;

// Internal
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.Board;

// Native
import java.util.List;

/*
 * @brief   The abstract class that each Piece movement behavior should implement
 * @author  Team5
 * @date    2017/03/09
 */
public abstract class PieceLogic {
    protected Piece pieceRef;

    public void setPieceRef(Piece ref) { pieceRef = ref; }
   
    public abstract List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc);		//return empty list if no moves

    public boolean coordinateWithinBounds(Board b, PieceCoordinate pc) {
        int rows = b.getRowCount(),
            cols = b.getColumnCount();

        if(pc.getRow() < 0 || pc.getRow() >= rows)
            return false;

        if(pc.getColumn() < 0 || pc.getColumn() >= cols)
            return false;

        return true;
    }

    public void setPieceReference(Piece p) { pieceRef = p; }

}
