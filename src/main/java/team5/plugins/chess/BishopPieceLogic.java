package team5.plugins.chess;

import team5.game.state.Board;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by james on 3/10/17.
 */
public class BishopPieceLogic extends PieceLogic {
    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();

        List<Pair<Integer, Integer>> rawCoords = new ArrayList<Pair<Integer, Integer>>() {{
            add(new Pair<>(+1, +1));
            add(new Pair<>(-1, +1));
            add(new Pair<>(+1, -1));
            add(new Pair<>(-1, -1));
        }};

        for (Pair<Integer, Integer> delta : rawCoords) {
            PieceCoordinate workingPC = new PieceCoordinate(pc.getRow(), pc.getColumn());

            while (true) {
                workingPC.setRow(workingPC.getRow() + delta.getFirst());
                workingPC.setColumn(workingPC.getColumn() + delta.getSecond());

                if (b.validCoordinate(workingPC)) {

                    Piece existingPiece = b.getPiece(workingPC);
                    if (existingPiece == null) {
                        // If the space to the current diagonal exists and is empty, add it
                        coords.add(new PieceCoordinate(workingPC.getRow(), workingPC.getColumn()));
                    } else if (!existingPiece.getUsername().equals(pieceRef.getUsername())) {
                        // If that space happens to have a piece that is NOT ours
                        // then we can capture it
                        coords.add(new PieceCoordinate(workingPC.getRow(), workingPC.getColumn()));
                        // and we can't go farther in this direction
                        break;
                    } else {
                        // It's one of our pieces, can't go farther in this direction
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

}
