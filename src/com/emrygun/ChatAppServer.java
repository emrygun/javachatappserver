package com.emrygun;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatAppServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    public ChatAppServer(int port){
        this.port = port;
    }

    public void execute(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Chat Server is listening port: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New User Connected");

                UserThread newUser = new UserThread(socket, this);
                userThreads.add(newUser);
                newUser.start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void broadcast(String message, UserThread excludeUser) {
        for (UserThread aUser : userThreads){
                aUser.sendMessage(message);
        }
    }

    void addUserName(String username) {
        userNames.add(username);
    }

    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println("The user " + userName + " quited.");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }
}
