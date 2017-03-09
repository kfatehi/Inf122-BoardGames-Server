package team5.network;


// Internal imports
import team5.game.GameSession;
import team5.game.GameLogicFactory;
import team5.game.GameManagerSingleton;
import team5.game.User;
import team5.game.GameStat;

// Native
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.ArrayList;

// External
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;


/**
 *
 * @brief   Purpose of this class is to handle client messages and
 *          respond to them.
 *  
 * Created by james on 3/7/17.
 */
public class CommunicationBridge {
    private GameSession gameSession;
    private String username;
    private Session wsSession;

    public CommunicationBridge(Session s) {
        // Initially, there will be gameSession/username associated with the client.
        // They will need to invoke the action "login" or "create game".
        gameSession = null;
        username = null;
        wsSession = s;
    }

    public void parseMessage(String json) {
        JsonParser jParser = new JsonParser();
        try {
            JsonObject entireJsonMsg = jParser.parse(json).getAsJsonObject();
            String type = entireJsonMsg.get("type").getAsString();

            if(type.equals("LOGIN")) {
                loginHandler(entireJsonMsg);
            } else if(type.equals("GET_USER_PROFILE")) {
                viewPersonalStats(entireJsonMsg);
            } else if (type.equals("CREATE_GAME")) {
                createGameHandler(entireJsonMsg);
            } else if (type.equals("JOIN_GAME")) {
                joinGameHandler(entireJsonMsg);
            } else if (type.equals("GET_ALL_SUPPORTED_GAMES")) {
                getSupportedGamesHandler(entireJsonMsg);
            } else if (type.equals("GET_OPEN_GAMES")) {
                listOpenGamesHandler(entireJsonMsg);
            } else if (type.equals("GET_OPEN_GAMES")) {

            } else {
                // Print stub
                System.out.println("Cannot handle provided type");
            }

        } catch(JsonParseException e) {
            System.out.println("String is not in the form of json");
            e.printStackTrace();
        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
        

    }

    public void sendMessage(JsonObject json) {
        if(wsSession != null && wsSession.isOpen()) {
            try {
                wsSession.getRemote().sendString(json.toString());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setGameSession(GameSession gameSession) {
        this.gameSession = gameSession;
    }

    /*
     * @brief   Handles the propagated json object and responds back to client
     */
    private void loginHandler(JsonObject json) {
        String  typeKey = "type",
                errMsgKey = "errorMessage";

        JsonObject successfulResponseJson = new JsonObject();
        successfulResponseJson.addProperty(typeKey, "LOGIN_SUCCESS");
        JsonObject failedResponseJson = new JsonObject();
        failedResponseJson.addProperty(typeKey, "LOGIN_ERROR");

        try {

            String username = json.get("username").getAsString();
            
            // Fail safe..
            if(username == null) {
                failedResponseJson.addProperty(errMsgKey, "Missing username");
                sendMessage(failedResponseJson);
                return;
            }

            if(GameManagerSingleton.instance().login(username)) {
                this.username = username;
                sendMessage(successfulResponseJson);
            } else {
                // Fail safe..
                failedResponseJson.addProperty(errMsgKey, "Unexpected error logging in.");
                sendMessage(failedResponseJson);
            }

        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void viewPersonalStats(JsonObject json) {
        String  typeKey = "type",
                usernameKey = "username",
                gamesKey = "games",

                gameTypeKey = "gameType",
                gamesWonKey = "gamesWon",
                gamesLostKey = "gamesLost",
                gamesDrawKey = "gamesDraw";

        JsonObject responseJson = new JsonObject();
        responseJson.addProperty(typeKey, "SET_USER_PROFILE");

        try {
            String username = json.get("username").getAsString();
            responseJson.addProperty(usernameKey, username);


            //User userObj = GameManagerSingleton.instance().user(username);
            //Test driver
            User userObj = new User();

            // Fail safe..
            if(username == null) {
                System.out.println("CommunicationBridge-viewPersonalStates: No username found in json object");
                return;
            }

            JsonArray jsonGamesArray = new JsonArray();

            //Build response
            if(userObj == null) {
                responseJson.add(gamesKey, jsonGamesArray);
            } else {
                // Fix these when suer is flusehd out
                GameStat g1 = new GameStat("Tic Tac Toe");
                GameStat g2 = new GameStat("Checkers");
                GameStat g3 = new GameStat("Chess");

                g1.incrementWins();
                g2.incrementLosses();
                g3.incrementDraws();

                ArrayList<GameStat> arrayTest = new ArrayList<GameStat>();
                arrayTest.add(g1);
                arrayTest.add(g2);
                arrayTest.add(g3);

                for(int i = 0; i < arrayTest.size(); i++) {
                    JsonObject gameStatJson = new JsonObject();
                    GameStat g = arrayTest.get(i);
                    gameStatJson.addProperty(gameTypeKey, g.getGameName());
                    gameStatJson.addProperty(gamesWonKey, g.getWins());
                    gameStatJson.addProperty(gamesLostKey, g.getLosses());
                    gameStatJson.addProperty(gamesDrawKey, g.getDraws());
                    jsonGamesArray.add(gameStatJson);
                }

                responseJson.add(gamesKey, jsonGamesArray);

            }

            sendMessage(responseJson);



        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void createGameHandler(JsonObject json) {

    }

    private void joinGameHandler(JsonElement json) {

    }

    private void listOpenGamesHandler(JsonElement json) {
        String  typeKey = "type",
                openGamesKey = "openGames",

                gameIDKey = "id",
                gameNameKey = "name",
                gamePlayersKey = "players",
                gameMaxPlayersKey = "maxPlayers",
                gameImageKey = "image";


        JsonObject responseJson = new JsonObject();
        responseJson.addProperty(typeKey, "SET_OPEN_GAMES");

        JsonArray openGamesJsonArray = new JsonArray();

        try {
            /*
            int numbOpenGames = GameManagerSingleton.getNumWaitingGames();
            
            for (int i = 0; i < numbOpenGames; i++) {
                JsonObject  openGameJson = new JsonObject();
                GameSession gameSession = GameManagerSingleton.getOpenGame(i);
                int numPlayers = gameSession.numPlayers();

                openGameJson.addProperty(gameIDKey, gameSession.id());
                openGameJson.addProperty(gameNameKey, gameSession.pugName());
//              openGameJson.addProperty(gameMaxPlayersKey, supportedGames.maxPlayers(gameSession.pugName()));
                openGameJson.addProperty(gameMaxPlayersKey, 2);
//              openGameJson.addProperty(gameImageKey, supportedGames.image(gameSession.pugName()));
                openGameJson.addProperty(gameImageKey, "");

                JsonArray playersJsonArray = new JsonArray();
                for (int j = 0; j < numPlayers; j++) {
                    playersJsonArray.add(gameSession.getUsername(j));
                }
                openGameJson.add(gamePlayersKey, playersJsonArray);

                openGamesJsonArray.add(openGameJson);
            }
            */
            responseJson.add(openGamesKey, openGamesJsonArray);
            
            
            sendMessage(responseJson);
            
            

        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void getSupportedGamesHandler(JsonElement json) {
    	String	typeKey = "type",
    			supportedGamesKey = "games",
    	
    			gameNameKey = "name",
    			gameMaxPlayersKey = "maxPlayers",
    			gameImageKey = "image";
    	
    	JsonObject responseJson = new JsonObject();
    	responseJson.addProperty(typeKey, "SET_ALL_SUPPORTED_GAMES");
    	
    	JsonArray supportedGamesJsonArray = new JsonArray();
    	
    	try {
    		List<String> supportedGames = GameLogicFactory.getAllSupportedGames();
    		int numSupportedGames = supportedGames.size();
    		
    		for (int i = 0; i < numSupportedGames; i++) {
    			JsonObject supportedGameJson = new JsonObject();
    			supportedGameJson.addProperty(gameNameKey, supportedGames.get(i));
//    			supportedGameJson.addProperty(gameMaxPlayersKey, GameLogicFactory.getMaxPlayers(supportedGames.get(i)));
    			supportedGameJson.addProperty(gameMaxPlayersKey, 2);
//				supportedGameJson.addProperty(gameImageKey, GameLogicFactory.getImage(supportedGames.get(i)));
    			supportedGameJson.addProperty(gameImageKey, "");
    			
    			supportedGamesJsonArray.add(supportedGameJson);
    		}
    		
    		responseJson.add(supportedGamesKey, supportedGamesJsonArray);
    		
    		sendMessage(responseJson);
    		
    		
    		
        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void clientTurnHandler(JsonObject json) {

    }

    private void sendStateChange() {

    }

    private void sendGameEnd(String winner, String message) {

    }

    public String username() { return username; }
}
