package team5.piece.logic.chess;

import team5.piece.logic.PieceMovementLogic;

public class Pawn implements PieceMovementLogic {
    public Pawn() {
        System.out.println("Building pawn logic");
    }

    public boolean validMode() {
        return true;
    }
}
