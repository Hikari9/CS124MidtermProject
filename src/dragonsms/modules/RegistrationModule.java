package dragonsms.modules;

import com.elegantsms.annotations.DispatchPriority;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.Priority;
import com.elegantsms.framework.SmsInjection;
import com.elegantsms.framework.SmsModule;

import dragonsms.session.SessionManager;

//@RegexDebug
public class RegistrationModule implements SmsModule {

    @SmsInjection
    SessionManager manager;

    private String name; // stores the name of user in the session

    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        manager.endSession();
        this.name = name;
        if (manager.getRepository().findOne(name) == null)
            return "Hello, " + name + ", welcome to DragonSMS. Send START to play.";
        else
            return "Welcome back, " + name + ". Send CONTINUE to resume from your previous game, or START to start a new game.";
    }

    @SmsQuery("CONTINUE")
    public String continueSession() {
        if (name == null || !manager.getRepository().exists(name))
            return "Invalid command, have you registered or started a session before?";
        manager.startNewSession(name);
        name = null;
        return manager.checkRoom(manager.getSession().getRoomName());
    }

    @SmsQuery("START")
    public String start() {
        if (name == null)
            return "Cannot start session. Make sure you register your name first.";
        if (manager.getRepository().exists(name))
            manager.getRepository().delete(name);
        manager.startNewSession(name); // starts new session
        name = null;
        return manager.checkRoom(manager.getSession().getRoomName()); // checkout Room1
    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("END")
    String end() {
        String sessionName = manager.getSession().toString();
        manager.endSession();
        return "Ended session: " + sessionName;
    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("EXIT")
    String exit() {
        manager.endSession();
        System.exit(0);
        return "";
    }

}
