package team5.game.state;

/**
 * Created by james on 3/8/17.
 */
public class Board {

    private Piece[][] board;
    private int rows;
    private int cols;


    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        board = new Piece[cols][rows];
    }

    public Piece getPiece(PieceCoordinate coord) {
        // Get the Piece at that coordinate
        return board[coord.getColumn()][coord.getRow()];
    }

    public PieceCoordinate getPiece(int id) {
        // Get the coordinate of the piece by its id
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (board[c][r].getId() == id) {
                    return new PieceCoordinate(r, c);
                }
            }
        }
        return null;
    }

    public Piece addPiece(Piece piece, PieceCoordinate coord) {
        // This returns to the caller a piece that happened to have already
        // been in that spot.
        Piece old = getPiece(coord);
        board[coord.getColumn()][coord.getRow()] = piece;
        return old;
    }

    public Piece movePiece(PieceCoordinate coord1, PieceCoordinate coord2) {
        // Move the piece at coord1 to coord2, returning what was at coord2
        Piece thePiece = getPiece(coord1);
        board[coord1.getColumn()][coord1.getRow()] = null;
        return addPiece(thePiece, coord2);
    }

    public Piece movePiece(int id, PieceCoordinate newCoord) {
        PieceCoordinate oldCoord = getPiece(id);
        return movePiece(oldCoord, newCoord);
    }
}
