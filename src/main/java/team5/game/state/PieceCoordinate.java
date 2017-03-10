package team5.game.state;

/**
 * Created by james on 3/8/17.
 */
public class PieceCoordinate {
    private int row;
    private int col;

    public PieceCoordinate() {}
    public PieceCoordinate(int r, int c) {
        row = r;
        col = c;
    }

    public void setRow(int r) { row = r; }
    public void setColumn(int c) { col = c; }

    public int getRow() { return this.row; }
    public int getColumn() { return this.col; }
}
