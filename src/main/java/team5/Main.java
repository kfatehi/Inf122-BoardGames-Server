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
        webSocket("/games", MainWebSocketHandler.class);
        init();

        // Showing that nested packages work
        new Pawn();
        new CommunicationBridge();
        new ChessGameLogic();
        new GameLogicFactory();
        new GameManagerSingleton();
        new GameSession();
        new User();




    }
}
