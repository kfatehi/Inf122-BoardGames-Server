package team5.plugins.chess;

import team5.game.state.Board;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 3/10/17.
 */
public class BishopPieceLogic extends PieceLogic {
    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        return new ArrayList<PieceCoordinate>();
    }

    public String canChangeToPiece() {
        return null;
    }
}
