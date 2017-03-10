package team5.game.state;

/**
 * Created by james on 3/8/17.
 */
public class Piece {
    private static int NEXT_UNIQUE_ID = 0;
    
    private int id;

    public Piece() {
        this.id = Piece.NEXT_UNIQUE_ID; 
        Piece.NEXT_UNIQUE_ID += 1;
    }

    public int getId() { return id; }
}
