package de.hsw.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;

public class MyBufferedReader extends BufferedReader {

    private final Socket socket;

    public MyBufferedReader(Socket socket) throws IOException {
        super(new InputStreamReader(socket.getInputStream()));
        this.socket = socket;
    }

    @Override
    public String readLine() throws IOException {
        String input = super.readLine();
        System.out.printf("Read from ip " + socket.getInetAddress() + ": %s%n", input);
        if(input == null) {
            try {
                throw new RuntimeException("Egal");
            } catch (Exception e) {
                e.printStackTrace();
            }
           // System.exit(0);
        }
        return input;
    }
}
