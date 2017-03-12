package team5.plugins.test;

import team5.game.GameLogic;
import team5.game.GameSession;
import team5.game.PieceLogicFactory;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.util.Pair;

/*
 * @brief   Game Logic for a simple test game
 * @author  Team5
 * @date    2017/03/08
 */
public class TestGameLogic extends GameLogic {
    private static final int ROWS = 2;
    private static final int COLS = 2;
    private String player1;
    private String player2;
    
    public TestGameLogic(GameSession session) {
        super(session);
        System.out.println("Creating TestGameLogic");

        gameName = "Test";
    }

    public team5.util.Pair<Integer, Integer> getBoardSize() {
    	return new Pair<Integer, Integer>(5, 5);
	}

    public void initializePieces() {
    	player1 = session.getUsernames().get(0);
    	player2 = session.getUsernames().get(1);
    	for(int i = 0; i < 4; i++){
    		Piece p = new Piece();
    		p.setPieceLogic(PieceLogicFactory.createPieceLogic("TestPieceLogic"));
    		p.setUsername(player1);
    		p.setImage("http://i.imgur.com/MK41sNi.jpg");
    		state().newUserPoolPiece(p, player1);
    	}
    	for(int i = 0; i < 4; i++){
    		Piece p = new Piece();
    		p.setPieceLogic(PieceLogicFactory.createPieceLogic("TestPieceLogic"));
    		p.setUsername(player2);
    		p.setImage("http://i.imgur.com/MK41sNi.jpg");
    		state().newUserPoolPiece(p, player2);
    	}
    }

    public void commitTurn(String username, int pieceId, PieceCoordinate intendedCoord) {
    	state().movePieceToBoard(pieceId, intendedCoord);
    	if(username.equals(player1)){
    		session.switchTurn(player2);
    	}
    	else session.switchTurn(player1);
    }

    public String gameFinishedWinner() {
    	if(session.gameState().pieceExistsAt(new PieceCoordinate(0,0))){
    		return session.gameState().getPieceAt(new PieceCoordinate(0,0)).getUsername();
    	}
        return null;
    }
}
