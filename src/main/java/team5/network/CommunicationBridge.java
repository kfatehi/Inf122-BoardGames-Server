package team5.network;


// Internal imports
import team5.game.GameSession;
import team5.game.GameLogicFactory;
import team5.game.GameManagerSingleton;
import team5.game.User;
import team5.game.state.Board;
import team5.game.state.Piece;
import team5.game.state.PieceCoordinate;
import team5.game.state.PiecePool;
import team5.game.GameStat;

// Native
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// External
import org.eclipse.jetty.websocket.api.Session;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonParseException;


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
            } else if (type.equals("TURN")) {
                clientTurnHandler(entireJsonMsg);
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
                System.out.println(json.toString());
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

            if(GameManagerSingleton.instance().login(username, this)) {
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
            String username = json.get(usernameKey).getAsString();
            responseJson.addProperty(usernameKey, username);


            User userObj = GameManagerSingleton.instance().user(username);

            // Fail safe..
            if(userObj == null) {
                System.out.println("CommunicationBridge-viewPersonalStates: No username found in json object");
                return;
            }

            JsonArray jsonGamesArray = new JsonArray();					//TODO: something similar for game state change
            
            for (GameStat stat : userObj.getStats()) {
                JsonObject gameStatJson = new JsonObject();
                gameStatJson.addProperty(gameTypeKey, stat.getGameName());
                gameStatJson.addProperty(gamesWonKey, stat.getWins());
                gameStatJson.addProperty(gamesLostKey, stat.getLosses());
                gameStatJson.addProperty(gamesDrawKey, stat.getDraws());
                jsonGamesArray.add(gameStatJson);
            }

            responseJson.add(gamesKey, jsonGamesArray);
            sendMessage(responseJson);


        } catch(ClassCastException e) {
            e.printStackTrace();
        } catch(IllegalStateException e) {
            e.printStackTrace();
        }
    }

    private void createGameHandler(JsonObject json) {
        String typeKey = "type",
               responseTypeKey = "NEWLY_CREATED_GAME",
               gameNameKey = "gameName",
               pugNameKey = "pugName",
               gameIdKey = "gameId";

        try {
            String gameName = json.get(gameNameKey).getAsString();
            String pugName = json.get(pugNameKey).getAsString();

            // Create it
            GameSession gameSession = GameManagerSingleton.instance().createGameSession(this, gameName, pugName);
            if (gameSession == null) {
                System.out.println("Unexpected error creating game session");
                return;
            }
            this.gameSession = gameSession;


            // Handle the response
            JsonObject response = new JsonObject();
            response.addProperty(typeKey, responseTypeKey);
            response.addProperty(gameNameKey, gameName);
            response.addProperty(gameIdKey, gameSession.id());
            // Send it
            sendMessage(response);


            // Also, push a new list of open games for the client for ease on its end
            listOpenGamesHandler(null);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void joinGameHandler(JsonObject json) {
        String typeKey = "type",
                gameIdKey = "gameId",
                joinSuccessVal = "JOIN_GAME_SUCCESS",
                joinFailedVal = "JOIN_GAME_FAILED",
                errorMessageKey = "errorMessage";
    	
        try {

            int gameId = json.get(gameIdKey).getAsInt();

            gameSession = GameManagerSingleton.instance().joinGameSession(this, gameId);
            if (gameSession == null) {
                // Error!
                JsonObject error = new JsonObject();
                error.addProperty(typeKey, joinFailedVal);
                error.addProperty(errorMessageKey, "Unexpected error joining game");
                sendMessage(error);
                return;
            }

            // Success
            JsonObject response = new JsonObject();
            response.addProperty(typeKey, joinSuccessVal);
            sendMessage(response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listOpenGamesHandler(JsonElement json) {
        String  typeKey = "type",
                openGamesKey = "openGames",

                gameIDKey = "id",
                gameNameKey = "name",
                gamePugNameKey = "pugName",
                gamePlayersKey = "players",
                gameMaxPlayersKey = "maxPlayers",
                gameImageKey = "image";


        JsonObject responseJson = new JsonObject();
        responseJson.addProperty(typeKey, "SET_OPEN_GAMES");

        JsonArray openGamesJsonArray = new JsonArray();

        try {
            
            List<GameSession> waitingGames = GameManagerSingleton.instance().getGamesWaiting();
            int numOpenGames = waitingGames.size();
            
            // Creating JsonObjects with info about each open game
            // to add to openGamesJsonArray
            for (int i = 0; i < numOpenGames; i++) {
                JsonObject  openGameJson = new JsonObject();
                GameSession gameSession = waitingGames.get(i);
                String gameName = gameSession.gameName();
                List<String> usernames = gameSession.getUsernames();
                int numPlayers = usernames.size();

                openGameJson.addProperty(gameIDKey, gameSession.id());
                openGameJson.addProperty(gameNameKey, gameName);
                openGameJson.addProperty(gamePugNameKey, gameSession.pugName());
              	openGameJson.addProperty(gameMaxPlayersKey, GameLogicFactory.getMaxPlayers(gameName));
              	openGameJson.addProperty(gameImageKey, GameLogicFactory.getImageURL(gameName));

                JsonArray playersJsonArray = new JsonArray();
                for (int j = 0; j < numPlayers; j++) {
                    playersJsonArray.add(usernames.get(j));
                }
                openGameJson.add(gamePlayersKey, playersJsonArray);

                openGamesJsonArray.add(openGameJson);
            }
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
    			String supportedGameName = supportedGames.get(i);
    			supportedGameJson.addProperty(gameNameKey, supportedGameName);
    			supportedGameJson.addProperty(gameMaxPlayersKey, GameLogicFactory.getMaxPlayers(supportedGameName));
				supportedGameJson.addProperty(gameImageKey, GameLogicFactory.getImageURL(supportedGameName));
    			
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
        String typeKey = "type",
                pieceIdKey = "pieceID",
                rowKey = "r",
                colKey = "c";

        if (username == null || username.equals("")) {
            System.out.println("Trying to send client turn but this comm bridge isn't associated with a User(name)");
            return;
        }
        if (gameSession == null) {
            System.out.println("Trying to send client turn but this comm bridge isn't associated with a GameSession");
            return;
        }


        int pieceId = json.get(pieceIdKey).getAsInt();
        int row = json.get(rowKey).getAsInt();
        int col = json.get(colKey).getAsInt();

        gameSession.userTurn(username, pieceId, new PieceCoordinate(row, col));
    }

    public void sendStateChange(String currentTurn, String turnType, Board b, PiecePool userPool, JsonArray diffs) {
    	/*
    	 * TODO for completing this function
    	 * 		-need diffs to be implemented
    	 * 		-need some sort of game logic to properly test the messages this is sending, *seems* to work with test data
    	 * */
    	JsonObject stateChangeJson = new JsonObject();
    	JsonArray validPlacements = new JsonArray();
    	JsonArray validMovements = new JsonArray();
    	stateChangeJson.addProperty("type", "SET_GAME_STATE");
    	stateChangeJson.addProperty("turn", currentTurn);
    	stateChangeJson.addProperty("turn_type", turnType);
    	if(turnType.equals("place")){
    		for(Piece p : userPool.getAllPiecesInPool()){			//for all pieces in the user's pool, add valid placements for all
    			for(PieceCoordinate coord : p.getPieceLogic().moveableCoordinates(b, new PieceCoordinate(-1,-1))){
    				JsonObject perPieceJson = new JsonObject();
    				perPieceJson.addProperty("r", coord.getRow());			//let me know if you need this to be a unique list instead, pieces can potentially overlap move spots right now 
    				perPieceJson.addProperty("c", coord.getColumn());
    				validPlacements.add(perPieceJson);
    			}
    		}
    		
    	}
    	else if(turnType.equals("move")){	//insert into valid movements
    		
    		for(Piece p : b.getAllPieces()){				//for every piece that has valid moves
    			JsonObject moveEntry = new JsonObject();
    			moveEntry.addProperty("pieceId", p.getId());
    			JsonArray moveList = new JsonArray();
    			for(PieceCoordinate coord : b.getLegalMovesOfPiece(p.getId())){		//for list of valid moves
    				JsonObject validCoords = new JsonObject();
    				validCoords.addProperty("r", coord.getRow());					//add move
    				validCoords.addProperty("c", coord.getColumn());
    				moveList.add(validCoords);
    			}
    			if(moveList.size() != 0){					//if a piece has valid moves, then write to main JSON object, otherwise ignore
    				moveEntry.add("moves", moveList);
        			validPlacements.add(moveEntry);
    			}
    		}
    	}
    	stateChangeJson.add("valid_placements", validPlacements);
    	stateChangeJson.add("valid_movements", validMovements);
    	stateChangeJson.add("board", b.parseBoardIntoJson());		//dumps entire board state into message
    	
    	JsonArray piecePoolArray = new JsonArray();
    	for(Piece p : userPool.getAllPiecesInPool()){		//gets state of piece pool
    		JsonObject piece = new JsonObject();
    		piece.addProperty("id", p.getId());
    		piece.addProperty("owner", p.getUsername());
    		piece.addProperty("image", p.getImage());
    		piecePoolArray.add(piece);
    	}
    	stateChangeJson.add("user_pool", piecePoolArray);
    	stateChangeJson.add("diffs", diffs);	//this will just be blank for now
    	sendMessage(stateChangeJson);
    }

    public void sendGameEnd(String winner) {	//overloaded to allow for easier plugin management, don't have to pass an empty string on game win if you don't want a message
    	JsonObject gameEndJson = new JsonObject();
    	String endType = "type", endWin = "winner", endMsg = "message";
    	gameEndJson.addProperty(endType, "GAME_END");
    	gameEndJson.addProperty(endWin, winner);
    	gameEndJson.addProperty(endMsg, "");
    	sendMessage(gameEndJson);
    }

    public void sendGameEnd(String winner, String message) {
    	JsonObject gameEndJson = new JsonObject();
    	String endType = "type", endWin = "winner", endMsg = "message";
    	gameEndJson.addProperty(endType, "GAME_END");
    	gameEndJson.addProperty(endWin, winner);
    	gameEndJson.addProperty(endMsg, message);
    	sendMessage(gameEndJson);
    }

    public void sendGameStart(int gameId, String[] opponents, boolean needsFlip, boolean checkered, int rows, int cols) {
        JsonObject gameStartJson = new JsonObject();
        String startType = "type",
                gameIdKey = "gameId",
                opponentsKey = "opponents",
                needsFlipKey = "needsFlip",
                checkeredKey = "checkered",
                rowsKey = "boardRows",
                colsKey = "boardCols";

        JsonArray opponentsArray = new JsonArray();
        for (String opponent : opponents) opponentsArray.add(opponent);

        gameStartJson.addProperty(startType, "GAME_INIT");
        gameStartJson.addProperty(gameIdKey, gameId);
        gameStartJson.add(opponentsKey, opponentsArray);
        gameStartJson.addProperty(needsFlipKey, needsFlip);
        gameStartJson.addProperty(checkeredKey, checkered);
        gameStartJson.addProperty(rowsKey, rows);
        gameStartJson.addProperty(colsKey, cols);

        sendMessage(gameStartJson);
    }

    public void sendGameStateChange() {
        JsonObject gameStateChangeJson = new JsonObject();
        String stateChangeType = "type";

        gameStateChangeJson.addProperty(stateChangeType, "SET_GAME_STATE");

        JsonArray opponentsArray = new JsonArray();
        sendMessage(gameStateChangeJson);
    }

    public String username() { return username; }

    public void setUsername(String username) {
        this.username = username;
    }

}
