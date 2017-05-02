package dragonsms;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

/**
 * Input/output stream wrapper that follows Strategy design pattern.
 */
public class IOStream {

    private InputStream in;
    private OutputStream out;
    private BufferedReader reader;
    private BufferedWriter writer;

    public IOStream() {
        this(System.in, System.out);
    }

    public IOStream(InputStream in, OutputStream out) {
        setIn(in);
        setOut(out);
    }

    public String readLine() {
        try {
            return getReader().readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void printf(String format, Object... args) {
        print(String.format(format, args));
    }

    public void print(Object line) {
        BufferedWriter writer = getWriter();
        try {
            writer.write(line == null ? "null" : line.toString());
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void println(Object line) {
        print(line);
        print("\n");
    }

    public void println() {
        println("");
    }

    public InputStream getIn() {
        return in;
    }

    public void setIn(InputStream in) {
        this.in = in;
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public OutputStream getOut() {
        return out;
    }

    public void setOut(OutputStream out) {
        this.out = out;
        writer = new BufferedWriter(new OutputStreamWriter(out));
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void close() {
        try {
            reader.close();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
