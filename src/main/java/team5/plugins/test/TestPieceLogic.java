package team5.plugins.test;

// Internal
import team5.game.state.PieceLogic;
import team5.game.state.PieceCoordinate;
import team5.game.state.Board;

// Native
import java.util.ArrayList;
import java.util.List;

/*
 * @author  Team5
 * @date    2017/03/09
 */
public class TestPieceLogic extends PieceLogic {
    public TestPieceLogic() {
        System.out.println("Building TestPieceLogic");
    }

    public List<PieceCoordinate> moveableCoordinates(Board b, PieceCoordinate pc) {
        if(pc.getColumn() != -1 && pc.getRow() != -1) return new ArrayList<PieceCoordinate>();
        ArrayList<PieceCoordinate> valid = new ArrayList<PieceCoordinate>();
        //00, 01, 10, 11
    	for(int i = 0; i < 2; i++){
    		for(int j = 0; j < 2; j++){
    			PieceCoordinate p = new PieceCoordinate(i, j);
    			if(b.getPiece(p) == null){
    				valid.add(p);
    			}
    		}
    	}
    	return valid;
    }

}
