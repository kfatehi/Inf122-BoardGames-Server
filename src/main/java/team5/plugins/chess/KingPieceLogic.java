package team5.plugins.chess;

import team5.game.state.Board;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PieceLogic;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by james on 3/10/17.
 */
public class KingPieceLogic extends FirstMovePieceLogic {

    private Map<PieceCoordinate, Pair<Piece, PieceCoordinate>> castleableRooksAndPos = new HashMap<>();

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        castleableRooksAndPos.clear();
        List<PieceCoordinate> coords = new ArrayList<PieceCoordinate>();

        List<Pair<Integer, Integer>> rawCoords = new ArrayList<Pair<Integer, Integer>>() {{
            add(new Pair<>(+1, +0));
            add(new Pair<>(+1, +1));
            add(new Pair<>(+0, +1));
            add(new Pair<>(-1, +1));
            add(new Pair<>(-1, +0));
            add(new Pair<>(-1, -1));
            add(new Pair<>(+0, -1));
            add(new Pair<>(+1, -1));
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

        // Castling
        // If the King hasn't moved yet, and it a Rook to the side of the King hasn't moved yet
        // and there's nothing between them then a Castling can happen
        int distance = 0;
        for (int colDelta : new int[]{-1,1}) {
            distance = 1;
            while (true) {
                PieceCoordinate destCoord = new PieceCoordinate(pc.getRow(), pc.getColumn() + colDelta*distance);

                if (!b.validCoordinate(destCoord)) {
                    break;
                }
                Piece existingPiece = b.getPiece(destCoord);
                if (existingPiece == null) {
                    // Empty square: keep going
                } else if (existingPiece.getUsername().equals(pieceRef.getUsername()) &&
                           RookPieceLogic.class.isInstance(existingPiece.getPieceLogic()) &&
                           distance > 2 &&
                           !hasMovedYet &&
                           !((RookPieceLogic)existingPiece.getPieceLogic()).hasMovedYet()) {
                    // Friendly rook is more than two spaces away and both this King and the Rook
                    // haven't moved yet
                    PieceCoordinate newCoord = new PieceCoordinate(pc.getRow(), pc.getColumn() + colDelta*2);
                    coords.add(newCoord);
                    castleableRooksAndPos.put(newCoord, new Pair<>(existingPiece,
                            new PieceCoordinate(pc.getRow(), pc.getColumn() + colDelta)));
                    System.out.println("moveableCoordinates: castleableRooksAndPos: " + castleableRooksAndPos);
                    break;
                } else {
                    // Castle not possible
                    break;
                }

                distance += 1;
            }
        }
        return coords;
    }

    public Piece movedFromTo(PieceCoordinate coord1, PieceCoordinate coord2) {
        // Mark that we've moved
        hasMovedYet = true;

        System.out.println("coord2: " + coord2);
        System.out.println("movedFromTo: castleableRooksAndPos: " + castleableRooksAndPos);

        for (PieceCoordinate pc : castleableRooksAndPos.keySet()) {
            if (pc.equals(coord2)) {
                return castleableRooksAndPos.get(pc).getFirst();
            }
        }
        return null;
    }

    public PieceCoordinate rookPos(PieceCoordinate newKingPos) {
        System.out.println("newKingPos: " + newKingPos);
        System.out.println("rookPos: castleableRooksAndPos: " + castleableRooksAndPos);
        for (PieceCoordinate pc : castleableRooksAndPos.keySet()) {
            if (pc.equals(newKingPos)) {
                return castleableRooksAndPos.get(pc).getSecond();
            }
        }
        return null;
    }

}
