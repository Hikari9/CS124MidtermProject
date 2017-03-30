package me.ricotiongson.dragonsms;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import me.ricotiongson.dragonsms.modules.DragonModule;
import me.ricotiongson.elegantsms.framework.SmsApplication;
import me.ricotiongson.elegantsms.framework.SmsPatternMismatchException;

/**
 * Automatically replies to stream of messages.
 */
public class DragonServer extends Thread {

    public final SmsApplication app;
    public final BufferedReader in;
    public final PrintStream out;

    public DragonServer(InputStream input, OutputStream output) {
        this.app = SmsApplication.loadModules(DragonModule.class);
        this.in = new BufferedReader(new InputStreamReader(input));
        this.out = new PrintStream(output);
    }

    @Override
    public void run() {
        // read input indefinitely
        in.lines().forEachOrdered(line -> {
            String reply = null;
            try {
                reply = app.getReply(line);
            } catch (SmsPatternMismatchException e) {
                reply = "invalid command: " + e.getMessage();
            }
            out.println(reply);
        });
    }

}
