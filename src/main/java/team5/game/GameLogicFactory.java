package team5.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

// Interal
import team5.game.GameLogic;
import team5.plugins.chess.*;
import team5.plugins.checkers.*;
import team5.plugins.tictactoe.*;
import team5.plugins.test.*;

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
            add("Test");
        }
    };
    
    private static Map<String, String> IMAGE_MAP = new HashMap<String, String>() {
    	{
    		put("Tic Tac Toe", "https://d30y9cdsu7xlg0.cloudfront.net/png/96852-200.png");
    		put("Checkers", "https://d30y9cdsu7xlg0.cloudfront.net/png/139786-200.png");
    		put("Chess", "/Chess.png");
    		put("Test", "http://i.imgur.com/Tvfwy6j.png");
    	}
    };
    
    private static Map<String, Integer> MAX_PLAYERS_MAP = new HashMap<String, Integer>() {
    	{
    		put("Tic Tac Toe", 2);
    		put("Checkers", 2);
    		put("Chess", 2);
    		put("Test", 2);
    	}
    };


    public GameLogicFactory() {
    }

    public GameLogic createGameLogic(String supportedGameName, GameSession gameSession) {
        if(supportedGameName.equalsIgnoreCase("Checkers"))
            return new CheckersGameLogic(gameSession);
        else if(supportedGameName.equalsIgnoreCase("Tic Tac Toe"))
            return new TicTacToeGameLogic(gameSession);
        else if(supportedGameName.equalsIgnoreCase("Chess"))
            return new ChessGameLogic(gameSession);
        else if (supportedGameName.equalsIgnoreCase("Test"))
            return new TestGameLogic(gameSession);
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
