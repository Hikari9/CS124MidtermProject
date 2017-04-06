package dragonsms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import com.elegantsms.framework.SmsApplication;

/**
 * Automatically replies to stream of messages.
 */
public class DragonServer extends Thread {

    // Driver program
    public static void main(String[] args) {

        // load Spring
        SpringDriver.run();

        // create dragon server from System I/O
        new DragonServer(System.in, System.out).start();
        System.out.println("Welcome to DragonSMS! Send HINT to get started!");

    }

    public final SmsApplication app;
    public final BufferedReader in;
    public final PrintStream out;

    public DragonServer(InputStream input, OutputStream output) {
        this.app = SmsApplication.loadPackage(getClass().getPackage().getName() + ".modules");
        this.in = new BufferedReader(new InputStreamReader(input));
        this.out = new PrintStream(output);
    }

    @Override
    public void run() {
        // read input indefinitely
        in.lines().forEachOrdered(line -> {
            String reply = app.getReplyNoThrow(line);
            if (reply == null)
                reply = "Invalid command. Send \"HINT\" for a list of possible commands.";
            out.println(reply);
        });
    }

}
