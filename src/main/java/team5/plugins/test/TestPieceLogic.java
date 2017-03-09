package team5.plugins.test;

import team5.game.state.PieceMovementLogic;

public class TestPieceLogic implements PieceMovementLogic {
    public TestPieceLogic() {
        System.out.println("Building TestPieceLogic");
    }

    public boolean validMode() {
        return true;
    }
}
