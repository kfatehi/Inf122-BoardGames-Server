package team5.game.state;

import org.eclipse.jetty.websocket.api.Session;
import team5.network.CommunicationBridge;

import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by james on 3/8/17.
 */
public class PiecePool {
    //Map<Piece.id, Piece> where Integer is Piece.id
    private Map<Integer, Piece> pieces;

    public PiecePool() {
        this.pieces = new HashMap<Integer, Piece>();
    }

    public PiecePool(ArrayList<Piece> pList) {
        this.pieces = new HashMap<Integer, Piece>();
        for(Piece p: pList)
            this.addPiece(p);
    }

    public Piece getPiece(int id) {
        return pieces.get(id);
    }

    public Piece getRandomPiece() {
        int max = pieces.size();
        //check if there are pieces in the piece pool otherwise return null
        if( max != 0) {
            Random ran = new Random();
            return this.removePiece(ran.nextInt(max));
        }
        return null;
    }

    public void addPiece(Piece p) {
        //pieces.put(p.getId(), p);
    }

    public Piece removePiece(int id) {
        return pieces.remove(id);
    }
}

