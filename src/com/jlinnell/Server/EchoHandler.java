package com.jlinnell.Server;

import com.jlinnell.Util;
import com.sun.net.httpserver.*;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;


/**
 * Created by james on 3/4/17.
 */
class EchoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange t) throws IOException {
        String response = "RequestMethod: " + t.getRequestMethod() + "\n" +
                "Headers:\n";
        for (String headerKey : t.getRequestHeaders().keySet()) {
            List<String> headerValues = t.getRequestHeaders().get(headerKey);
            response += headerKey + ": " + headerValues + "\n";
        }
        response += "Body: " + Util.inputStreamToString(t.getRequestBody());

        // Need to set the response headers first, which means we need
        // to have our entire response ready before hand to get the length.
        t.sendResponseHeaders(200, response.length());
        t.getResponseBody().write(response.getBytes());
        t.getResponseBody().close();
    }
}
