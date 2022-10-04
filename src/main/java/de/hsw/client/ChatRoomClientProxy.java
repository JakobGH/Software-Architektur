package de.hsw.client;

import de.hsw.shared.IChatRoom;
import de.hsw.shared.IChatter;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ChatRoomClientProxy implements IChatRoom {

    private final MyBufferedReader reader;
    private final MyPrintWriter writer;

    private Map<IChatter, String> alreadyHandledChatters;

    public ChatRoomClientProxy(MyBufferedReader reader, MyPrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
        this.alreadyHandledChatters = new HashMap<>();
    }

    @Override
    public void joinRoom(IChatter chatter) {
        readProtocol();
        writer.println("0");
        sendChatter(chatter);
        readResponse();
    }

    @Override
    public void writeMessage(IChatter chatter, String message) {
        readProtocol();
        writer.println("1");
        sendChatter(chatter);
        readProtocol();
        writer.println(message);
        readResponse();
    }

    @Override
    public void leave(IChatter chatter) {
        readProtocol();
        writer.println("2");
        sendChatter(chatter);
        readResponse();
    }

    public void technicalDisconnect() {
        readProtocol();
        writer.println("3");
        readProtocol();
    }

    private void sendChatter(IChatter chatter) {
        readProtocol();

        if(alreadyHandledChatters.containsKey(chatter)) {
             writer.println(alreadyHandledChatters.get(chatter));
        } else {
            String uuid = UUID.randomUUID().toString();
            alreadyHandledChatters.put(chatter, uuid);
            writer.println(uuid);
            try {
                ServerSocket socket = new ServerSocket(0);
                int localPort = socket.getLocalPort();
                writer.println(String.valueOf(localPort));
                Socket accept = socket.accept();
                MyPrintWriter myPrintWriter = new MyPrintWriter(new PrintWriter(accept.getOutputStream()));
                MyBufferedReader myBufferedReader = new MyBufferedReader(accept);
                Thread fred = new Thread(new ChatterServerProxy(chatter, myBufferedReader, myPrintWriter));
                fred.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            readProtocol();
            writer.println(chatter.getUsername());
        }
    }

    private void readResponse() {
        String status = readLine();

        if(status.equals("2")) {
            String error = readLine();
            throw new RuntimeException(error);
        }
    }

    private void readProtocol() {
        try {
            String protocol = reader.readLine();
            //System.out.printf("Protocol: " + protocol);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
