package com.emrygun;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //Check for args count
        if (args.length < 1) {
            System.out.println("Syntax: java ChatAppServer <port>");
            System.exit(0);
        }

        //Init server and run
        int port = Integer.parseInt(args[0]);
        ChatAppServer server = new ChatAppServer(port);
        server.execute();
    }
}
