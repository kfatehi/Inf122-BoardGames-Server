package team5;

import static spark.Spark.*;

import team5.game.state.MovementDirection;
import team5.game.state.Piece;
import team5.game.state.PieceLogic;
import team5.network.MainWebSocketHandler;
//import team5.plugins.chess.PawnPieceLogic;

// Does not do recursive import.
import team5.game.*;

// This is why this is needed in order to import ChessGameLogic
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


        PieceLogic pawn = PieceLogicFactory.createPieceLogic("Pawn");
        Piece p = new Piece("jlinnell", "pawn.png", pawn, MovementDirection.Down);
    }

    public static void startServer() {
        webSocket("/games", MainWebSocketHandler.class);
        init();
    }
}
