package de.hsw.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerRuntime {

    public static void main(String[] args) {

        ChatRoom chatRoom = new ChatRoom();

        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(42169);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        do {
            try {
                Socket clientSocket = serverSocket.accept();

                ChatRoomServerProxy chatRoomServerProxy = new ChatRoomServerProxy(clientSocket, chatRoom); //TODO: In Liste packen
                Thread t = new Thread(chatRoomServerProxy);
                t.start();

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } while (true); //TODO: Admin Fred einbauen
    }

}
