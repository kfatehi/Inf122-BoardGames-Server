package team5.plugins.tictactoe;

//Internal
import team5.game.state.PieceLogic;
import team5.game.state.PieceCoordinate;

import java.util.ArrayList;
import java.util.List;

import team5.game.state.Board;

/*
 * @author  Team5
 * @date    2017/03/11
 */

public class TicTacToePieceLogic extends PieceLogic {
	
	public TicTacToePieceLogic () {
        System.out.println("Building TicTacToePieceLogic");
	}

	public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        ArrayList<PieceCoordinate> valid = new ArrayList<PieceCoordinate>();
		// tic-tac-toe piece should not be able to move once on the board
		if (pc.getRow() != -1 && pc.getColumn() != -1) {
			return valid;
		}
        for (int row = 0; row < 3; row++) {
        	for (int col = 0; col < 3; col++) {
        		PieceCoordinate coordinate = new PieceCoordinate(row, col);
        		if (b.getPiece(coordinate) == null) {
        			valid.add(coordinate);
        		}
        	}
        }
        return valid;
	}


}
