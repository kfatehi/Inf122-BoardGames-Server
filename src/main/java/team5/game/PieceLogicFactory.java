package team5.game;

// Internal
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;
import team5.plugins.checkers.*;
import team5.plugins.chess.*;
import team5.plugins.test.TestPieceLogic;
import team5.plugins.tictactoe.TicTacToePieceLogic;

// Native
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class PieceLogicFactory {

    private static Map<String, Class> classMap = new HashMap<String, Class>() {{
        // Chess
        put("Bishop", BishopPieceLogic.class);
        put("King", KingPieceLogic.class);
        put("Knight", KnightPieceLogic.class);
        put("Pawn", PawnPieceLogic.class);
        put("Queen", QueenPieceLogic.class);
        put("Rook", RookPieceLogic.class);

        // Test Game
        put("TestPieceLogic", TestPieceLogic.class);
        
        // Tic-Tac-Toe Game
        put("TicTacToePiece", TicTacToePieceLogic.class);

        // Checkers Game
        put("Checker", CheckerPieceLogic.class);
    }};

    public PieceLogicFactory() {}

    public static PieceLogic createPieceLogic(String pieceName) {
        Class theClass = classMap.get(pieceName);
        if (theClass != null) {
            try {
                return (PieceLogic)theClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            System.out.println("Error: Attempting to make ");
            return null;
        }
    }


}
