package de.hsw.client;

import de.hsw.server.ChatterMock;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientRuntime {

    public static void main(String[] args) {


        try {
            Socket socket = new Socket("localhost", 42169);
            MyBufferedReader reader = new MyBufferedReader(new InputStreamReader(socket.getInputStream()));
            MyPrintWriter writer = new MyPrintWriter(new PrintWriter(socket.getOutputStream()));
            ChatRoomClientProxy chatRoomClientProxy = new ChatRoomClientProxy(reader, writer);
            ChatterMock gandalf = new ChatterMock("Frodo");
            chatRoomClientProxy.joinRoom(gandalf);
            Scanner sc = new Scanner(System.in);
            String input = "";
            while(!input.equals("Ende")) {
                System.out.println("Gib eine Nachricht ein. Zum Beenden 'Ende' eingeben: ");
                input = sc.nextLine();
                chatRoomClientProxy.writeMessage(gandalf, input);
            }
            chatRoomClientProxy.leave(gandalf);
            chatRoomClientProxy.technicalDisconnect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
