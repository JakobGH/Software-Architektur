package de.hsw.server;

import de.hsw.shared.IChatRoom;
import de.hsw.shared.IChatter;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class ChatRoomServerProxy implements Runnable {

    private boolean isRunning = true;
    private final BufferedReader reader;
    private final BufferedWriter writer;
    private final IChatRoom chatRoom;
    private final Map<String, IChatter> alreadyDeserializedChatters;


    public ChatRoomServerProxy(BufferedReader reader, BufferedWriter writer, IChatRoom chatRoom) {
        this.reader = reader;
        this.writer = writer;
        this.chatRoom = chatRoom;
        alreadyDeserializedChatters = new HashMap<>();
    }

    @Override
    public void run() {
        while (isRunning) {

            writeMessage("0: Join, 1: Write Message, 2: Leave, 3: Technical Disconnect");
            String response = readLine();

            switch (response) {
                case "0" -> join();
                case "1" -> write();
                case "2" -> leave();
                case "3" -> technicalDisconnect();
                default -> error();
            }
        }
    }

    private void writeMessage(String message) {
        try {
            writer.write(message + "\n");
            writer.flush();
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

    private void join() {
        IChatter chatterFromResponse = getChatterFromResponse();
        try {
            chatRoom.joinRoom(chatterFromResponse);
            writeMessage("1");
        } catch (Exception e) {
            writeMessage("2");
            writeMessage(e.getMessage());
        }
    }

    private IChatter getChatterFromResponse() {
        writeMessage("Provide identifier");
        String identifierResponse = readLine();

        if (alreadyDeserializedChatters.containsKey(identifierResponse)) {
            return alreadyDeserializedChatters.get(identifierResponse);
        }

        writeMessage("Gib Port alder");
        try {
            int port = Integer.parseInt(readLine());
            Socket socket = new Socket("localhost", port); //TODO: Ip mitliefern
            MyBufferedReader myBufferedReader = new MyBufferedReader(new InputStreamReader(new BufferedInputStream(socket.getInputStream())));
            MyPrintWriter myPrintWriter = new MyPrintWriter(new PrintWriter(socket.getOutputStream()));
            ChatterClientProxy chatterClientProxy = new ChatterClientProxy(myBufferedReader, myPrintWriter);
            alreadyDeserializedChatters.put(identifierResponse, chatterClientProxy);
            writeMessage("Provide username");
            String usernameResponse = readLine();
            return alreadyDeserializedChatters.get(identifierResponse);
        } catch (NumberFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void write() {

        IChatter chatter = getChatterFromResponse();
        writeMessage("Provide message");
        String message = readLine();
        try {
            chatRoom.writeMessage(chatter, message);
            writeMessage("1");
        } catch (Exception e) {
            writeMessage("2");
            writeMessage(e.getMessage());
        }
    }

    private void leave() {

        IChatter chatter = getChatterFromResponse();
        try {
            chatRoom.leave(chatter);
            writeMessage("1");
        } catch (Exception e) {
            writeMessage("2");
            writeMessage(e.getMessage());
        }
    }

    private void technicalDisconnect() {
        writeMessage("Closing connection now");
        isRunning = false;
    }

    private void error() {
        writeMessage("Could not interpret the sent message ;(");
    }
}
