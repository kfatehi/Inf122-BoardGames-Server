package team5.game;

// Internal
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;

// Native
import java.util.Hashtable;
import java.util.Map;
import java.util.Map.Entry;

public class PieceLogicFactory {

    public PieceLogicFactory() {}

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
