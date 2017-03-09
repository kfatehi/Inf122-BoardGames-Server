package team5.game;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

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
    
    private static HashMap<String, String> IMAGE_MAP = new HashMap<String, String>() {
    	{
    		put("Tic Tac Toe", "https://d30y9cdsu7xlg0.cloudfront.net/png/96852-200.png");
    		put("Checkers", "https://d30y9cdsu7xlg0.cloudfront.net/png/139786-200.png");
    		put("Chess", "https://d30y9cdsu7xlg0.cloudfront.net/png/24034-200.png");
    	}
    };
    
    private static HashMap<String, Integer> MAX_PLAYERS_MAP = new HashMap<String, Integer>() {
    	{
    		put("Tic Tac Toe", 2);
    		put("Checkers", 2);
    		put("Chess", 2);
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
    
    public static String getImageURL(String supportedGameName) {
    	return IMAGE_MAP.get(supportedGameName);
    }
    
    public static int getMaxPlayers(String supportedGameName) {
    	return MAX_PLAYERS_MAP.get(supportedGameName);
    }
    
}
