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
    private MovementDirection direction;

    public Piece() {
        this.id = Piece.NEXT_UNIQUE_ID; 
        Piece.NEXT_UNIQUE_ID += 1;
        direction = MovementDirection.Up; // Default direction
    }

    public Piece(String username, String image, MovementDirection dir) {
        this.id = Piece.NEXT_UNIQUE_ID;
        Piece.NEXT_UNIQUE_ID += 1;

        this.username = username;
        this.image = image;
        this.direction = dir;
    }

    public Piece(String username, String image, PieceLogic logic, MovementDirection dir) {
        this.id = Piece.NEXT_UNIQUE_ID;
        Piece.NEXT_UNIQUE_ID += 1;

        this.username = username;
        this.image = image;
        this.pieceLogic = logic;
        this.direction = dir;
    }

    public int getId() { return id; }
    public String getUsername() { return username; }
    public String getImage() { return image; }
    public PieceLogic getPieceLogic() { return pieceLogic; }
    public MovementDirection getDirection() { return direction; }

    public void setUsername(String name) { this.username = name; }
    public void setImage(String image) { this.image = image; }
    public void setPieceLogic(PieceLogic logic) { this.pieceLogic = logic; }
    public void setDirection(MovementDirection direction) { this.direction = direction; }
}
