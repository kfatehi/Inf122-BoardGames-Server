package team5;

import static spark.Spark.*;

import team5.game.state.*;
import team5.network.MainWebSocketHandler;
//import team5.plugins.chess.PawnPieceLogic;

// Does not do recursive import.
import team5.game.*;

// This is why this is needed in order to import ChessGameLogic
import team5.plugins.checkers.CheckerPieceLogic;
import team5.plugins.chess.ChessGameLogic;

/**
 * Created by james on 3/5/17.
 */
public class Main {

    public static void main(String[] args) throws Exception {

        startServer();

        for(String game : GameLogicFactory.getAllSupportedGames()) {
            System.out.println("We currently support " + game);
        }

        //PieceLogicFactory pl = new PieceLogicFactory();
        //pl.createPieces("tic tac toe");
        //pl.createPieces("Checkers");
        //pl.createPieces("CHESS");



        Piece p = new Piece("alvin", "pawn.png", MovementDirection.Down);
        PieceLogic checkerLogic = new CheckerPieceLogic(p);
        p.setPieceLogic(checkerLogic);

        Board b = new Board(8,8);
        b.addPiece(new Piece(), new PieceCoordinate(6,1));

        System.out.println("Valid movements");
        PieceCoordinate startPos = new PieceCoordinate(7,0);
        for(PieceCoordinate pc : p.getPieceLogic().moveableCoordinates(b, startPos)) {
            System.out.println("Row: " + String.valueOf(pc.getRow()) + " Col: " + String.valueOf(pc.getColumn()));
        }
    }

    public static void startServer() {
        webSocket("/games", MainWebSocketHandler.class);
        init();
    }
}
