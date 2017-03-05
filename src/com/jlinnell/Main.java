package com.jlinnell;

import com.jlinnell.Server.Server;

public class Main {

    public static void main(String[] args) throws Exception {


        Server server = new Server(8080);
        server.serve();


    }

}
