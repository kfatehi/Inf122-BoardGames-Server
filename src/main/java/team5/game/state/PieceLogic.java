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
    private Piece pieceRef;
   
    public abstract List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc);		//return empty list if no moves 
    public abstract String canChangeToPiece();
}
