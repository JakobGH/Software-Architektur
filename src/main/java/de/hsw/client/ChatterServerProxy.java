package de.hsw.client;

import de.hsw.shared.IChatter;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.IOException;

public class ChatterServerProxy implements Runnable {

    private final IChatter chatter;
    private final MyBufferedReader reader;
    private final MyPrintWriter writer;

    private boolean isRunning;

    public ChatterServerProxy(IChatter chatter, MyBufferedReader reader, MyPrintWriter writer) {
        this.chatter = chatter;
        this.reader = reader;
        this.writer = writer;
        this.isRunning = true;
    }

    @Override
    public void run() {
        while (isRunning) {
            writer.println("0: getName, 1: receiveMessage, 2: technicalDisconnect");
            String response = readLine();

            switch (response) {
                case "0" -> getName();
                case "1" -> receiveMessage();
                case "2" -> technicalDisconnect();
                default -> error();
            }
        }
    }

    private void getName() {
        try {
            String username = chatter.getUsername();
            writer.println("1");
            writer.println(username);
        } catch (Exception e) {
            writer.println("2");
            writer.println(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    private void receiveMessage() {
        try {
            writer.println("Ey, alter gib message");
            String message = readLine();
            chatter.receiveMessage(message);
            writer.println("1");
        } catch (Exception e) {
            writer.println("2");
            throw new RuntimeException(e.getMessage());
        }
    }

    private void technicalDisconnect() {
        writer.println("Disconnecting now ;(");
        isRunning = false;
    }

    private void error() {
        System.err.println("Hier ist etwas schiefgelaufen!");
    }

    private String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
