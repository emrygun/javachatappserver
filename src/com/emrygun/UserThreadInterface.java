package com.emrygun;

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
        return U_MESSAGE + "[" + username + "]: " + clientmessage;
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
    default String disconnectMessage(String userName){
        return DISCONNECT + userName;
    }
}
