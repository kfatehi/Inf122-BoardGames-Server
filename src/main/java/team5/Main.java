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

        // Showing that nested packages work
//        new PawnPieceLogic();
        //new GameManagerSingleton();
        //new CommunicationBridge();
//        new ChessGameLogic();
        GameLogicFactory gl = new GameLogicFactory();
//        gl.createGameLogic("Tic Tac Toe");
//        gl.createGameLogic("Checkers");
//        gl.createGameLogic("Chess");
//        gl.createGameLogic("Test Game");

        for(String game : GameLogicFactory.getAllSupportedGames()) {
            System.out.println("We currently support " + game);
        }

        //PieceLogicFactory pl = new PieceLogicFactory();
        //pl.createPieces("tic tac toe");
        //pl.createPieces("Checkers");
        //pl.createPieces("CHESS");



        Piece p = new Piece("alvin", "pawn.png", MovementDirection.Up);
        PieceLogic checkerLogic = new CheckerPieceLogic(p);
        p.setPieceLogic(checkerLogic);

        Board b = new Board(8,8);
        b.addPiece(new Piece(), new PieceCoordinate(1,1));

        System.out.println("Valid movements");
        PieceCoordinate startPos = new PieceCoordinate(0,0);
        for(PieceCoordinate pc : p.getPieceLogic().moveableCoordinates(b, startPos)) {
            System.out.println("Row: " + String.valueOf(pc.getRow()) + " Col: " + String.valueOf(pc.getColumn()));
        }
    }

    public static void startServer() {
        webSocket("/games", MainWebSocketHandler.class);
        init();
    }
}
