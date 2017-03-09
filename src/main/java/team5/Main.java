package team5;

import static spark.Spark.*;
import team5.network.MainWebSocketHandler;
import team5.plugins.chess.Pawn;
import team5.network.CommunicationBridge;
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
        new Pawn();
        //new GameManagerSingleton();
        //new CommunicationBridge();
        new ChessGameLogic();
        GameLogicFactory gl = new GameLogicFactory();
        gl.createGameLogic("Tic Tac Toe");
        gl.createGameLogic("Checkers");
        gl.createGameLogic("Chess");

        for(String game : GameLogicFactory.getAllSupportedGames()) {
            System.out.println("We currently support " + game);
        }
        //new GameSession();
//        new User();

    }

    public static void startServer() {
        webSocket("/games", MainWebSocketHandler.class);
        init();
    }
}
