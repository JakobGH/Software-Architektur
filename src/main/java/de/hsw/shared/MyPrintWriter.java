package de.hsw.shared;

import java.io.PrintWriter;
import java.io.Writer;

public class MyPrintWriter extends PrintWriter {

    public MyPrintWriter(Writer out) {
        super(out);
    }

    @Override
    public void println(String x) {
        super.println(x);
        super.flush();
        //System.out.printf("Write: %s%n", x);
    }
}
