package com.jlinnell.Server;

import com.sun.net.httpserver.*;

import java.net.InetSocketAddress;

/**
 * Created by james on 3/4/17.
 */
public class Server {

    HttpServer httpServer;

    public Server(int port) throws Exception {
        // Create the underlying http server
        httpServer = HttpServer.create(new InetSocketAddress(port), 0);

        // Create all the handlers for various API endpoints
        httpServer.createContext("/echo", new EchoHandler());

        // Default executor
        httpServer.setExecutor(null);
    }


    public void serve() {
        // Start the underlying server. This blocks.
        httpServer.start();
    }
}
