package team5.plugins.tictactoe;

// Internal
import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.state.Board;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PiecePool;
import team5.util.Pair;

import java.util.ArrayList;
import java.util.List;

/*
 * @brief   Game logic for Tic Tac Toe
 * @author  Team5
 * @date    2016/03/08
 */
public class TicTacToeGameLogic extends GameLogic {
    private static final int ROWS = 3;
    private static final int COLS = 3;
    private String player1;
    private String player2;
    
    public TicTacToeGameLogic(GameSession session) {
        super(session);
        System.out.println("Created TicTacToeGameLogic");
        
        gameName = "Tic Tac Toe";
    }

    public Pair<Integer, Integer> getBoardSize() {
        return new Pair<Integer, Integer>(3, 3);
    }

    public boolean needsCheckered() { return false; }
    public boolean needsFlip(String username) { return false; }

    public List<PieceCoordinate> getValidPlacements(String username) {
        ArrayList<PieceCoordinate> validPlacements = new ArrayList<PieceCoordinate>();

        Board b = session.gameState().getBoard();
        ArrayList<Piece> userPieces = session.gameState().getUserPiecePool(username).getAllPiecesInPool();
        if(userPieces.size() > 0) {
            return userPieces.get(0).getPieceLogic().moveableCoordinates(b, new PieceCoordinate(-1, -1));
        }

        return validPlacements;
    }

        public void initializePieces() {
    	player1 = session.getUsernames().get(0);
    	player2 = session.getUsernames().get(1);
    	for (int i = 0; i < 5; i++) {
    		Piece p = new Piece();
    		p.setUsername(player1);
    		p.setPieceLogic(PieceLogicFactory.createPieceLogic("TicTacToePiece"));
    		p.setImage("/TicTacToe_x.png");
    		state().newUserPoolPiece(p, player1);    		
    	}
       	for (int i = 0; i < 5; i++) {
    		Piece p = new Piece();
    		p.setUsername(player2);
    		p.setPieceLogic(PieceLogicFactory.createPieceLogic("TicTacToePiece"));
    		p.setImage("/TicTacToe_o.png");
    		state().newUserPoolPiece(p, player2);    		
    	}
    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
    	state().movePieceToBoard(pieceId, intendedCoord);
    	session.switchTurn((username.equals(player1)) ? player2 : player1);
    }

    public String gameFinishedWinner() {
    	if (checkRows() != null) {
    		return checkRows();
    	}
    	if (checkColumns() != null) {
    		return checkColumns();
    	}
    	if (checkDiagonals() != null) {
    		return checkDiagonals();
    	}
    	if (checkDraw() != null) {
    		return checkDraw();
    	}
    	return null;
    }
    
    private String getUsernameAtCoordinate(PieceCoordinate pc) {
    	if (session.gameState().pieceExistsAt(pc)) {
    		return session.gameState().getPieceAt(pc).getUsername();
    	}
    	return null;
    }
    
    private String checkRows() {
    	for (int row = 0; row < 3; row++) {
    		String previousPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(row, 0));
    		String currentPieceOwner;
    		// if first column does not have a piece, skip nested for loop
    		// cannot possibly 3 in a row if first column has no piece
    		if (previousPieceOwner == null) {
    			continue;
    		}
    		for (int col = 1; col < 3; col++) {
    			currentPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(row, col));
    			if (currentPieceOwner == null) {
    				break;
    			}
    			if (!currentPieceOwner.equals(previousPieceOwner)) {
    				break;
    			}
    			if (col == 2) {
    				return currentPieceOwner;
    			}
    			previousPieceOwner = currentPieceOwner;
    		}
    	}
    	return null;
    }
    
    private String checkColumns() {
    	for (int col = 0; col < 3; col++) {
    		String previousPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(0, col));
    		String currentPieceOwner;
    		
    		// if first row does not have a piece, skip nested for loop
    		// cannot possibly 3 in a row if first row has no piece
    		if (previousPieceOwner == null) {
    			continue;
    		}
    		for (int row = 1; row < 3; row++) {
    			currentPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(row, col));
    			if (currentPieceOwner == null) {
    				break;
    			}
    			if (!currentPieceOwner.equals(previousPieceOwner)) {
    				break;
    			}
    			if (row == 2) {
    				return currentPieceOwner;
    			}
    			previousPieceOwner = currentPieceOwner;
    		}
    	}
    	return null;
    }
    
    private String checkDiagonals() {
		String previousPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(0, 0));
		String currentPieceOwner;
		if (previousPieceOwner != null) {
	    	for (int i = 1; i < 3; i++) {
	    		currentPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(i, i));
    			if (currentPieceOwner == null) {
    				break;
    			}
    			if (!currentPieceOwner.equals(previousPieceOwner)) {
    				break;
    			}
    			if (i == 2) {
    				return currentPieceOwner;
    			}
    			previousPieceOwner = currentPieceOwner;
	    	}
		}
		previousPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(2, 0));
		if (previousPieceOwner != null) {
			for (int i = 1; i < 3; i++) {
				currentPieceOwner = getUsernameAtCoordinate(new PieceCoordinate(2 - i, i));
    			if (currentPieceOwner == null) {
    				break;
    			}
    			if (!currentPieceOwner.equals(previousPieceOwner)) {
    				break;
    			}
    			if (i == 2) {
    				return currentPieceOwner;
    			}
    			previousPieceOwner = currentPieceOwner;
			}
		}
    	return null;
    }
    
    private String checkDraw() {
    	for (int row = 0; row < 3; row++) {
    		for (int col = 0; col < 3; col++) {
    			if (getUsernameAtCoordinate(new PieceCoordinate(row, col)) == null) {
    				return null;
    			}
    		}
    	}
    	return "";
    }
}
