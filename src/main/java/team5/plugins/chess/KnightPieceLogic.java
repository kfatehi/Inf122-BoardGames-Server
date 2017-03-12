package team5.plugins.chess;

import javafx.util.Pair;
import team5.game.state.Board;
import team5.game.state.PieceCoordinate;

import java.util.ArrayList;
import java.util.IntSummaryStatistics;
import java.util.List;

/**
 * Created by james on 3/10/17.
 */
public class KnightPieceLogic {

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();

        List<Pair<Integer, Integer>> rawCoords = new List<Pair<Integer, Integer>>() {{
            add(new Pair<>(+2,+1));
            add(new Pair<>(+1, +2));
            add(new Pair<>(-1, +2));
            add(new Pair<>(-2, +1));
            add(new Pair<>(-2,-1));
            add(new Pair<>(-1, -2));
            add(new Pair<>(+1, -2));
            add(new Pair<>(+2, -1));
        }};

        

        return coords;
    }

    public String canChangeToPiece() {
        return null;
    }
}
