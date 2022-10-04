package de.hsw.server;

import de.hsw.shared.IChatter;
import de.hsw.shared.MyBufferedReader;
import de.hsw.shared.MyPrintWriter;

import java.io.IOException;

public class ChatterClientProxy implements IChatter {

    private final MyBufferedReader reader;
    private final MyPrintWriter writer;

    public ChatterClientProxy(MyBufferedReader reader, MyPrintWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    @Override
    public String getUsername() {

        try {
            reader.readLine();
            writer.println("0");
            String responseCode = reader.readLine();
            if (responseCode.equals("1")) {
                return reader.readLine();
            }
            throw new RuntimeException(reader.readLine());
        } catch (IOException e) {
            e.printStackTrace();
            return "Unknown";
            //throw new RuntimeException(e);
        }
    }

    @Override
    public void receiveMessage(String message) {
        try {
            reader.readLine();
            writer.println("1");
            reader.readLine();
            writer.println(message);
            String serverResponse = reader.readLine();
            if(serverResponse == null) {
                return;
            }
            if (!serverResponse.equals("1")) {
                throw new RuntimeException(1 + reader.readLine());
            }
        } catch (IOException e) {
            System.err.println(e.getMessage());
            //throw new RuntimeException(e);
        }
    }

    public void technicalDisconnect() {
        try {
            reader.readLine();
            writer.println("2");
            reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
    }
}
