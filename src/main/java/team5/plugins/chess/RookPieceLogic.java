package team5.plugins.chess;

import team5.game.state.Board;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 3/10/17.
 */
public class RookPieceLogic extends PieceLogic {

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();

        List<Pair<Integer, Integer>> rawCoords = new ArrayList<Pair<Integer, Integer>>() {{
            add(new Pair<>(1, 0));
            add(new Pair<>(-1, 0));
            add(new Pair<>(0, 1));
            add(new Pair<>(0, -1));
        }};

        for (Pair<Integer, Integer> delta : rawCoords) {
            PieceCoordinate workingPC = new PieceCoordinate(pc.getRow(), pc.getColumn());

            while (true) {
                workingPC.setRow(workingPC.getRow() + delta.getFirst());
                workingPC.setColumn(workingPC.getColumn() + delta.getFirst());

                if (b.validCoordinate(workingPC)) {
                    // If the space to the current direction exists, add it
                    coords.add(new PieceCoordinate(workingPC.getRow(), workingPC.getColumn()));

                    if (b.getPiece(workingPC) != null) {
                        // If that space also happens to have a piece, then this is the extent
                        // to which it can move in this direction
                        break;
                    }
                } else {
                    // If it's off the board, stop
                    break;
                }
            }
        }

        return coords;
    }

    public String canChangeToPiece() {
        return null;
    }
}
