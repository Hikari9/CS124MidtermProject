package dragonsms;

import com.elegantsms.framework.SmsApplication;

import dragonsms.session.SessionManager;

/**
 * Creates a thread that accepts a stream of messages.
 */
public class DragonServer extends Thread {

    public final IOStream io;

    public DragonServer() {
        this(new IOStream());
    }

    public DragonServer(IOStream io) {
        this.io = io;
    }

    @Override
    public void run() {
        SmsApplication app = SmsApplication.loadPackage(getClass().getPackage().getName() + ".modules");

        // inject session manager into the framework
        SessionManager manager = new SessionManager();
        app.inject("manager", manager);

        // read input indefinitely
        try {
            String line;
            while ((line = io.readLine()) != null) {
                String reply = app.getReplyNoThrow(line);
                if (reply == null)
                    reply = "Invalid command. Send \"HINT\" for a list of possible commands.";
                io.println(reply);
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            io.close();
        }
    }

}
