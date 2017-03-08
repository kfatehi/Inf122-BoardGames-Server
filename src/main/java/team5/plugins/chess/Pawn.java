package team5.plugins.chess;

import team5.game.state.PieceMovementLogic;

public class Pawn implements PieceMovementLogic {
    public Pawn() {
        System.out.println("Building pawn logic");
    }

    public boolean validMode() {
        return true;
    }
}
