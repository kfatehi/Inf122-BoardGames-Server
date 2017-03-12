package team5.game.state;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by james on 3/8/17.
 */
public class GameState {

    private Board board;
    private PiecePool serverPool = new PiecePool();
    private PiecePool capturedPool = new PiecePool();
    private Map<String, PiecePool> userPools = new HashMap<String, PiecePool>();
    private ArrayList<Integer> ids = new ArrayList<Integer>();

    public GameState(int rows, int cols) {
        board = new Board(rows, cols);
    }
    private boolean pieceExists(Piece piece){	//true if a piece with this ID has already been added to the game state, false if not
    	if(ids.contains(piece.getId())){		//checks to make sure a game isn't trying to add the same piece multiple times
    		return true;
    	}
    	return false;
    }
    public boolean pieceExistsAt(PieceCoordinate coord){	//note: if this fork becomes public, add to UML
    	if(board.getPiece(coord) != null){
    		return true;
    	}
    	return false;
    }
    public Piece getPieceAt(PieceCoordinate coord){
    	return board.getPiece(coord);
    }
    //returns true if this is a valid initial placement, false if a piece is already in this square
    //presumably, you don't want to place two pieces down on the same square while setting up the game
    public boolean newBoardPiece(Piece piece, PieceCoordinate coord) {
    	if(pieceExists(piece)){
    		return false;
    	}
    	Piece p = board.addPiece(piece, coord);
    	if(p != null){
    		board.addPiece(p, coord);
    		return false;
    	}
    	ids.add(piece.getId());
    	return true;
    }
    public boolean newServerPoolPiece(Piece piece) {
    	if(pieceExists(piece)) return false;
    	serverPool.addPiece(piece);
    	return true;
    }
    //this is probably unnecessary, but i'm leaving it
    public boolean newCapturedPoolPiece(Piece piece) {
    	if(pieceExists(piece)) return false;
    	capturedPool.addPiece(piece);
    	return true;
    }

    public boolean newUserPoolPiece(Piece piece, String username) {
    	if(pieceExists(piece)) return false;
    	if (userPools.keySet().contains(username) == false) {
    		userPools.put(username, new PiecePool());
		}
    	userPools.get(username).addPiece(piece);
    	return true;
    }
    //searches all pools for the given piece and adds it to the board
    //returns null if couldn't find a valid piece, else returns the piece you added to the board, or the piece that was in the same position, if there was such a piece
    public Piece movePieceToBoard(int id, PieceCoordinate coord) {
    	Piece p = null;
    	PiecePool foundPool = null;
    	boolean pieceFound = false;
    	if(serverPool.getPiece(id) != null){
    		p = serverPool.getPiece(id);
    		foundPool = serverPool;
    		pieceFound = true;
    	}
    	if(capturedPool.getPiece(id) != null){
    		if(pieceFound) return null;
    		p = capturedPool.getPiece(id);
    		foundPool = capturedPool;
    		pieceFound = true;
    	}
    	else{
    		for(PiecePool user : userPools.values()){
    			if(user.getPiece(id) != null){
    				if(pieceFound) return null;
    				p = user.getPiece(id);
    				foundPool = user;
    				pieceFound = true;
    			}
    		}
    	}
    	if(!pieceFound) return null;
    	foundPool.removePiece(id);
    	Piece p2 = board.addPiece(p, coord);
    	if(p2 == null) return p;
    	else return p2;
    }
    public boolean movePieceToUserPool(int id, String username) {
    	Piece p = null;
    	PiecePool foundPool = null;
    	boolean pieceFound = false;
    	boolean onBoard = false;
    	if(serverPool.getPiece(id) != null){
    		p = serverPool.getPiece(id);
    		foundPool = serverPool;
    		pieceFound = true;
    	}
    	if(capturedPool.getPiece(id) != null){
    		if(pieceFound) return false;
    		p = capturedPool.getPiece(id);
    		foundPool = capturedPool;
    		pieceFound = true;
    	}
    	if(board.getPiece(id) != null){
    		if(pieceFound) return false;
    		p = board.getPiece(board.getPiece(id));
    		onBoard = true;
    		pieceFound = true;
    	}
    	if(!pieceFound) return false;
    	if(onBoard){
    		board.removePiece(id);
    	}
    	else foundPool.removePiece(id);
    	if(userPools.get(username) != null){
    		userPools.get(username).addPiece(p);
    		return true;
    	}
    	return false;
    }

    public boolean capturePiece(int id) {
    	PieceCoordinate pc = board.getPiece(id);
    	if(pc == null) return false;
    	capturedPool.addPiece(board.getPiece(pc));
    	board.removePiece(id);
    	return true;
    }

}
