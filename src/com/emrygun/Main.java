package com.emrygun;

public class Main {
    public static void main(String[] args) {
        //Check for args count
        if (args.length < 1) {
            System.out.println("Syntax: java ChatAppServer <port-number>");
            System.exit(0);
        }

        //Init server and run
        int port = Integer.parseInt(args[0]);
        ChatAppServer server = new ChatAppServer(port);
        server.execute();
    }
}
