package team5.network;


// Internal imports
import team5.game.GameSession;
import team5.game.GameManagerSingleton;
import team5.game.User;

// Native
import java.io.IOException;
import java.util.Hashtable;

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
                JsonObject gameStatJson = new JsonObject();
                gameStatJson.addProperty(gameTypeKey, "Tic Tac Toe");
                gameStatJson.addProperty(gamesWonKey, 50);
                gameStatJson.addProperty(gamesLostKey, 0);
                gameStatJson.addProperty(gamesDrawKey, 10);

                jsonGamesArray.add(gameStatJson);
                responseJson.add(gamesKey, jsonGamesArray);

            }

            sendMessage(responseJson);



        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
