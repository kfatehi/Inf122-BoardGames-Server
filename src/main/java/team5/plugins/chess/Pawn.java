package team5.plugins.chess;

import team5.game.state.PieceLogic;

public class Pawn extends PieceLogic {
    public Pawn() {
        System.out.println("Building pawn logic");
    }

    public boolean validMode() {
        return true;
    }
}
