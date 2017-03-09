package team5.game;

import java.util.ArrayList;
import java.util.List;

// Interal
import team5.game.GameLogic;
import team5.plugins.chess.*;
import team5.plugins.checkers.*;
import team5.plugins.tictactoe.*;

/*
 * Handles the creation of game logic objects given a unique game name
 * @author  Team5
 * @date    2017/03/08 
 */
public class GameLogicFactory {
    private static ArrayList<String> SUPPORTED_GAMES = new ArrayList<String>() {
        {
            add("Tic Tac Toe");
            add("Checkers");
            add("Chess");
        }
    };


    public GameLogicFactory() {
    }

    public GameLogic createGameLogic(String supportedGameName) {
        if(supportedGameName.equalsIgnoreCase("Checkers"))
            return new CheckersGameLogic();
        else if(supportedGameName.equalsIgnoreCase("Tic Tac Toe"))
            return new TicTacToeGameLogic();
        else if(supportedGameName.equalsIgnoreCase("Chess"))
            return new ChessGameLogic();
        else
            return null;
    }

    public static List<String> getAllSupportedGames() {
        return GameLogicFactory.SUPPORTED_GAMES;
    }
    
}
