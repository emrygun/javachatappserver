package com.emrygun;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class UserThread extends Thread implements UserThreadInterface{
    //Create thread related Socket and Thread
    private Socket socket;
    private ChatAppServer server;

    //Create Thread related strings
    private String userName;
    private String serverMessage;
    private String clientMessage;

    //Create writers and stuff
    private PrintWriter writer;
    private InputStream input;
    private BufferedReader reader;
    private OutputStream output;


    //Constructor
    public UserThread(Socket socket, ChatAppServer server) throws IOException {
        this.socket = socket;
        this.server = server;

        //Bind inputstream and reader
        input = socket.getInputStream();
        reader = new BufferedReader(new InputStreamReader(input));

        //Bind outputstream and writer
        output = socket.getOutputStream();
        writer = new PrintWriter(output, true);
    }

    //Thread run
    public void run() {
        try {
            //Print users to related UserThread
            //So clients can add them into their userList
            printUsers();

            //Get username and add it
            userName = reader.readLine();

            if (userName.equals(null)) return;

            server.addUserName(userName);

            //Broadcast new connection
            newConnection();

            //Client and Server Message Handler
            do { broadcastUserMessage();
            } while(clientMessage != null);

            Disconnect();

        } catch (SocketException ex) {
            Disconnect();
        } catch (IOException ex) {
            System.out.println("UserThread Hatasi: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    //Prints users
    void printUsers() {
        if (server.hasUsers()) {
            writer.println(userListMessage(server));
        } else {
            writer.println(serverMessage("Katilan kullanici yok"));
        }
    }

    //Grant new connection
    void newConnection() {
        //Connection Message
        serverMessage =  serverMessage("Yeni kullanici katildi: " + userName);
        server.broadcast(serverMessage, this);

        //Send username so clients can add it to their list
        server.broadcast(connectionMessage(this.userName), this);
    }

    //Remove connection and close socket
    void Disconnect() {
        server.removeUser(userName,this);
        //Try to close socket
        try {
            socket.close();
        } catch (IOException e) {
            System.out.println("Soket kapatilamadi.");
        }

        serverMessage = serverMessage(userName + " sunucudan ayrildi.");
        server.broadcast(serverMessage, this);
        server.broadcast(disconnectMessage(this.userName), this);
    }

    //Get user message and if it's a user message broadcast it
    void broadcastUserMessage() throws IOException {
        if((clientMessage = reader.readLine()) != null) {
            server.broadcast(userMessage(this.userName, clientMessage), this);
        }
    }

}
