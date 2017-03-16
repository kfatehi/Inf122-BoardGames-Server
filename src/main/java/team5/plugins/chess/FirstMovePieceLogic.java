package team5.plugins.chess;

import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;

/**
 * Created by james on 3/15/17.
 */
public abstract class FirstMovePieceLogic extends PieceLogic {
    protected boolean hasMovedYet;

    public abstract Piece movedFromTo(PieceCoordinate coord1, PieceCoordinate coord2);

    public boolean hasMovedYet() { return hasMovedYet; }
}
