package team5.plugins.test;

import team5.game.state.PieceLogic;

public class TestPieceLogic extends PieceLogic {
    public TestPieceLogic() {
        System.out.println("Building TestPieceLogic");
    }

    public boolean validMode() {
        return true;
    }
}
