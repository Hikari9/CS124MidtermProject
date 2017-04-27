package dragonsms;

import com.elegantsms.framework.SmsApplication;
import com.elegantsms.framework.SmsPatternMismatchException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import dragonsms.session.SessionManager;

/**
 * Automatically replies to stream of messages.
 */
public class DragonServer extends Thread {

    public final BufferedReader in;
    public final PrintStream out;

    public DragonServer(InputStream input, OutputStream output) {
        this.in = new BufferedReader(new InputStreamReader(input));
        this.out = new PrintStream(output);
    }

    @Override
    public void run() {
        SmsApplication app = SmsApplication.loadPackage(getClass().getPackage().getName() + ".modules");

        // inject session manager into the framework
        SessionManager manager = new SessionManager();
        app.inject("manager", manager);

        // read input indefinitely
        in.lines().forEachOrdered(line -> {
            String reply = app.getReplyNoThrow(line);
            if (reply == null)
                reply = "Invalid command. Send \"HINT\" for a list of possible commands.";
            out.println(reply);
        });
    }

}
