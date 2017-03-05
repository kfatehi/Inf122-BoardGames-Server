package com.jlinnell;

import com.jlinnell.Server.Server;

public class Main {

    public static void main(String[] args) throws Exception {

        int port = 8080;
        Server server = new Server(port);
        System.out.println("Running server on http://localhost:"+port);
        server.serve();


    }

}
