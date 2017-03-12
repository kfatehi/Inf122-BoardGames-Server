package team5.game.state;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

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
    public Piece[][] getBoard(){
    	return board;
    }
    /**
     * 
     * @param b
     * @return board formatted into JSON object, 
     */
    public JsonArray parseBoardIntoJson(){
    	JsonArray boardJson = new JsonArray();
    	for(int i = 0; i < rows; i++){
    		JsonArray subArr = new JsonArray();
    		for(int j = 0; j < rows; j++){
    			JsonObject piece = new JsonObject();
    			if(board[i][j] == null){
    				piece = null;
    			}
    			else{
    				int pieceID = board[i][j].getId();
    				String owner = board[i][j].getUsername();
    				String image = board[i][j].getImage();
    				piece.addProperty("pieceId", pieceID);
    				piece.addProperty("owner", owner);
    				piece.addProperty("image", image);
    			}
    			subArr.add(piece);
    		}
    		boardJson.add(subArr);
    	}
    	return boardJson;
    }
    public Piece getPiece(PieceCoordinate coord) {
        // Get the Piece at that coordinate
        return board[coord.getColumn()][coord.getRow()];
    }

    public boolean validCoordinate(PieceCoordinate coord) {
        return (coord.getRow() >= 0 && coord.getRow() < rows) &&
                (coord.getColumn() >= 0 && coord.getColumn() < cols);
    }

    public PieceCoordinate getPiece(int id) {
        // Get the coordinate of the piece by its id
        for (int c = 0; c < cols; c++) {
            for (int r = 0; r < rows; r++) {
                if (board[c][r] != null && board[c][r].getId() == id) {
                    return new PieceCoordinate(r, c);
                }
            }
        }
        return null;
    }
    public List<PieceCoordinate> getLegalMovesOfPiece(int id){
    	PieceCoordinate location = getPiece(id);
    	if(location == null) return null;
    	else{
    		Piece p = getPiece(location);
    		return p.getPieceLogic().moveableCoordinates(this, location);
    	}
    }
    public ArrayList<Piece> getAllPieces(){
    	ArrayList<Piece> piecesOnBoard = new ArrayList<Piece>();
    	for(int c = 0; c < cols; c++){
    		for(int r = 0; r < rows; r++){
    			if(board[c][r] != null){
    				piecesOnBoard.add(board[c][r]);
    			}
    		}
    	}
    	return piecesOnBoard;
    }
    public Piece addPiece(Piece piece, PieceCoordinate coord) {
        // This returns to the caller a piece that happened to have already
        // been in that spot.
        Piece old = getPiece(coord);
        board[coord.getColumn()][coord.getRow()] = piece;
        return old;
    }
    public Piece removePiece(int id){
    	PieceCoordinate c = getPiece(id);
    	Piece p = board[c.getColumn()][c.getRow()];
    	board[c.getColumn()][c.getRow()] = null;
    	return p;
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
