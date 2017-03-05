package com.jlinnell.RESTServer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Created by james on 3/5/17.
 */
public class GamesHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "Placeholder. Hi.";

        // Need to set the response headers first, which means we need
        // to have our entire response ready before hand to get the length.
        t.sendResponseHeaders(200, response.length());
        t.getResponseBody().write(response.getBytes());
        t.getResponseBody().close();
    }
}
