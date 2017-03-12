package team5.game;

// Internal
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;
import team5.plugins.checkers.CheckerPieceLogic;
import team5.plugins.chess.Pawn;
import team5.plugins.test.TestPieceLogic;

// Native
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class PieceLogicFactory {

    private static Map<String, Class> classMap = new HashMap<String, Class>() {{
        put("Pawn", Pawn.class);
        put("TestPieceLogic", TestPieceLogic.class);
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
            return null;
        }
    }

    public Map<Integer, Piece> createPieces(String uniqueGameName) {
        Hashtable<Integer, Piece> gamePieces = new Hashtable<Integer, Piece>();
        if(uniqueGameName.equalsIgnoreCase("Tic Tac Toe")) {
            System.out.println("Creating pieces for Tic Tac Toe");

            for(int i = 0; i < 10; i++) {
                Piece p = new Piece();
                gamePieces.put(p.getId(), p);
            }
        } else if(uniqueGameName.equalsIgnoreCase("Chess")) {
            System.out.println("Creating pieces for Chess");

            for(int i = 0; i < 1; i++) {
                Piece p = new Piece();
                gamePieces.put(p.getId(), p);
            }
        } else if(uniqueGameName.equalsIgnoreCase("Checkers")) {
            System.out.println("Creating pieces for Checkers");

            for(int i = 0; i < 50; i++) {
                Piece p = new Piece();
                gamePieces.put(p.getId(), p);
            }

        } else {
            return null;
        }

        return gamePieces;
    }


}
