package team5.game.state;

/**
 * Created by james on 3/8/17.
 */
public class Piece {
    private static int NEXT_UNIQUE_ID = 0;
    
    private int id;
    private String username;
    private String image;
    private PieceLogic pieceLogic;

    public Piece() {
        this.id = Piece.NEXT_UNIQUE_ID; 
        Piece.NEXT_UNIQUE_ID += 1;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getImage() { return image; }
    public PieceLogic getPieceLogic() { return pieceLogic; }

    public void setUsername(String name) { this.username = name; }
    public void setImage(String image) { this.image = image; }
    public void setPieceLogic(PieceLogic logic) { this.pieceLogic = logic; }
}
