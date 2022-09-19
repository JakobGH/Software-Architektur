package de.hsw.client;

import de.hsw.server.ChatterMock;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientRuntime {

    public static void main(String[] args) {


        try {
            Socket socket = new Socket("localhost", 42169);
            MyBufferedReader reader = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));
            MyPrintWriter writer = new MyPrintWriter(new PrintWriter(socket.getOutputStream()));
            ChatRoomClientProxy chatRoomClientProxy = new ChatRoomClientProxy(reader, writer);
            ChatterMock gandalf = new ChatterMock("Frodo");
            chatRoomClientProxy.joinRoom(gandalf);
            chatRoomClientProxy.writeMessage(gandalf, "Hello, my name is Frodo");
            chatRoomClientProxy.technicalDisconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
