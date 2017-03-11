package team5.plugins.chess;

//Internal
import team5.game.state.Piece;
import team5.game.state.PieceLogic;
import team5.game.state.PieceCoordinate;
import team5.game.state.Board;

// Native
import java.util.ArrayList;
import java.util.List;

/*
 * @author  Team5
 * @date    2017/03/09
 */
public class Pawn extends PieceLogic {
    public Pawn() {
        System.out.println("Building pawn logic");
    }

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        return new ArrayList<PieceCoordinate>();    
    }

    public String canChangeToPiece() {
        return null;
    }
}
