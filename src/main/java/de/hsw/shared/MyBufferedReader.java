package de.hsw.shared;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class MyBufferedReader extends BufferedReader {

    public MyBufferedReader(Reader in) {
        super(in);
    }

    @Override
    public String readLine() throws IOException {
        String input = super.readLine();
        //System.out.printf("Read: %s%n", input);
        return input;
    }
}
