package de.hsw.server;

import java.io.IOException;
import java.net.ServerSocket;

public class ServerRuntime {

    public static void main(String[] args) {

        ChatRoom chatRoom = new ChatRoom();

        try {
            ServerSocket serverSocket = new ServerSocket(42069);
            

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

}
