package com.emrygun;

import java.awt.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public interface UserThreadInterface {
    //Status constants
    public static final int S_MESSAGE   = 1;
    public static final int U_MESSAGE   = 2;
    public static final int USERNAME    = 3;
    public static final int USERLIST    = 4;
    public static final int CONNECT     = 5;
    public static final int DISCONNECT  = 6;

    //Generate Messages to Broadcast
    default String serverMessage(String message) {
        return S_MESSAGE + message;
    }
    default String userMessage(String username, String clientmessage) {
        return U_MESSAGE + clientmessage.substring(0,11)+ "[" + username + "]: " + clientmessage.substring(11);
    }

    default String userListMessage(ChatAppServer server) {
        String returnMessage = String.valueOf(USERLIST);
        Iterator<String> t = server.getUserNames().iterator();
        //Create string of users
        do {
            returnMessage = returnMessage + t.next() ;
            if (t.hasNext()) returnMessage = returnMessage + "/1/";
        } while (t.hasNext());
        return returnMessage;
    }
    default String connectionMessage(String username) {
        return CONNECT + username;
    }
    default String disconnectMessage(String userName){ return DISCONNECT + userName; }

    default void writeLog(String userName, String message, FileWriter writer) throws IOException {
        Color messageColor = new Color(Integer.parseInt(message.substring(0,3)),
                Integer.parseInt(message.substring(3,6)),
                Integer.parseInt(message.substring(6,9)));
        int messageSize = Integer.parseInt(message.substring(9,11));
        writer.append(String.format("Color: R:%03d G:%03d B:%03d\tTextSize: %02d\t User: %s\t Message: %s\n",
                messageColor.getRed(), messageColor.getGreen(), messageColor.getBlue(), messageSize, userName,
                message.substring(11)));
        writer.flush();
    }
}
