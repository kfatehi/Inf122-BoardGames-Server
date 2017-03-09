package team5.network;


// Internal imports
import team5.game.GameSession;
import team5.game.GameManagerSingleton;

// Native
import java.io.IOException;

// External
import org.eclipse.jetty.websocket.api.Session;
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

    public CommunicationBridge() {
        // Initially, there will be gameSession/username associated with the client.
        // They will need to invoke the action "login" or "create game".
        gameSession = null;
        username = null;
        wsSession = null;
    }

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
            JsonElement typeJsonElem = entireJsonMsg.get("type");

            // Client Action Router
            if(typeJsonElem.getAsString().equals("LOGIN")) {
                loginHandler(entireJsonMsg);
            else if(typeJsonElem.getAsString().equals("GET_USER_PROFILE")) {
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

            JsonElement usernameJsonElem = json.get("username");
            String usernameAttempt = usernameJsonElem.getAsString();
            
            // Fail safe..
            if(usernameJsonElem == null) {
                failedResponseJson.addProperty(errMsgKey, "Missing username");
                sendMessage(failedResponseJson);
            }

            if(GameManagerSingleton.getSharedInstance().login(usernameAttempt)) {
                username = usernameAttempt;
                sendMessage(successfulResponseJson); 

            // Fail safe..
            } else {
                failedResponseJson.addProperty(errMsgKey, "");
                sendMessage(failedResponseJson);
            }

        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    public void viewPersonalStats(JsonObject json) {
    }
}
