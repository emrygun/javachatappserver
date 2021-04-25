package com.emrygun;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class ChatAppServer {
    private int port;
    private Set<String> userNames = new HashSet<>();
    private Set<UserThread> userThreads = new HashSet<>();

    //Create PrintWriter for logs
    private FileWriter logWriter = new FileWriter("logs/messageLogs.log");

    public ChatAppServer(int port) throws IOException {
        this.port = port;
    }

    public void execute(){
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Mesajlasma sunucusu ilgili portu dinliyor: " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Yeni Kullanici Katildi");

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

        try {
            writeLog(message, logWriter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void addUserName(String username) {
        userNames.add(username);
    }

    void removeUser(String userName, UserThread aUser) {
        boolean removed = userNames.remove(userName);
        if (removed) {
            userThreads.remove(aUser);
            System.out.println(userName + " sunucudan ayrildi.");
        }
    }

    Set<String> getUserNames() {
        return this.userNames;
    }

    boolean hasUsers() {
        return !this.userNames.isEmpty();
    }

    void writeLog(String message, FileWriter writer) throws IOException {
        Color messageColor = new Color(Integer.parseInt(message.substring(0,3)),
                Integer.parseInt(message.substring(3,6)),
                Integer.parseInt(message.substring(6,9)));
        int messageSize = Integer.parseInt(message.substring(9,11));
        writer.append(String.format("Renk: R:%03d G:%03d B:%03d\tYaziBoyu: %02d\tMesaj: %s\n",
                messageColor.getRed(), messageColor.getGreen(), messageColor.getBlue(), messageSize, message.substring(11)));
        writer.flush();
    }
}
