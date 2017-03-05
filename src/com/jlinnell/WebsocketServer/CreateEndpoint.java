package com.jlinnell.WebsocketServer;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * Created by james on 3/5/17.
 */
@ServerEndpoint("/game/{gamename}")
public class CreateEndpoint {
    @OnOpen
    public void onOpen(Session session, @PathParam("gamename") final String gameName) {

    }

    @OnMessage
    public void onMessage(String message, Session session) throws IOException {

    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {

    }
}
