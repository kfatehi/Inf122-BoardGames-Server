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



    }

    public static void startServer() {
        webSocket("/games", MainWebSocketHandler.class);
        staticFiles.location("/images");
        init();
    }
}
