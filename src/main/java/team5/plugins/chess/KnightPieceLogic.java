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
public class KnightPieceLogic extends PieceLogic {

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();

        List<Pair<Integer, Integer>> rawCoords = new ArrayList<Pair<Integer, Integer>>() {{
            add(new Pair<>(+2, +1));
            add(new Pair<>(+1, +2));
            add(new Pair<>(-1, +2));
            add(new Pair<>(-2, +1));
            add(new Pair<>(-2, -1));
            add(new Pair<>(-1, -2));
            add(new Pair<>(+1, -2));
            add(new Pair<>(+2, -1));
        }};

        for (Pair<Integer, Integer> delta : rawCoords) {
            PieceCoordinate destCoord = new PieceCoordinate(pc.getRow()+delta.getFirst(), pc.getColumn()+delta.getSecond());
            if (b.validCoordinate(destCoord)) {
                Piece existingPiece = b.getPiece(destCoord);
                if (existingPiece == null) {
                    // Empty square
                    coords.add(destCoord);
                } else if (!existingPiece.getUsername().equals(pieceRef.getUsername())) {
                    // Enemy piece is on the square
                    coords.add(destCoord);
                } else {
                    // Our own piece, can't capture them!
                    continue;
                }

            }
        }

        return coords;
    }

}
