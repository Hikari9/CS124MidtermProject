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
        // end current session and register a new name
        manager.endSession();
        this.name = name;
        if (manager.getRepository().exists(name))
            return "Welcome back, " + name + ". Send CONTINUE to resume from your previous game, or START to start a new game.";
        return "Hello, " + name + ", welcome to DragonSMS. Send START to play.";
    }

    @SmsQuery("CONTINUE")
    public String continueSession() {
        if (!manager.restoreSession(name))
            return "Invalid command, have you registered or started a session before?";
        name = null;
        return manager.checkRoom(manager.getSession().getRoomName());
    }

    @SmsQuery("START")
    public String start() {
        if (!manager.startNewSession(name))
            return "Cannot start session. Make sure you register your name first.";
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
