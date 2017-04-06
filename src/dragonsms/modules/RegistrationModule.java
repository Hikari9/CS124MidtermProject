package dragonsms.modules;

import com.elegantsms.annotations.DispatchPriority;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.Priority;
import com.elegantsms.framework.SmsModule;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import dragonsms.entities.Session;
import dragonsms.session.SessionManager;

//@RegexDebug
public class RegistrationModule extends SessionManager implements SmsModule {

    private String name; // stores the name of user in the session

    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        endSession();
        this.name = name;
        // check if user already exists
        if (getDao().findOne(name) == null)
            return "Hello, " + name + ", welcome to DragonSMS. Send START to play.";
        else
            return "Welcome back, " + name + ". Send CONTINUE to resume from your previous game, or START to start a new game.";
    }

    @SmsQuery("CONTINUE")
    public String continueSession() {
        if (name == null || getDao().findOne(name) == null)
            return "Invalid command, have you registered or started a session before?";
        startSession(name);
        name = null;
        return checkRoom(getSession().getRoomName());
    }

    @SmsQuery("START")
    public String start() {
        if (name == null)
            return "Cannot start session. Make sure you register your name first.";
        if (getDao().exists(name))
            getDao().delete(name);
        startSession(name); // starts new session
        name = null;
        return checkRoom(getSession().getRoomName()); // checkout Room1
    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("END")
    String end() {
        String sessionName = getSession().toString();
        endSession();
        return "Ended session: " + sessionName;
    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("EXIT")
    String exit() {
        endSession();
        System.exit(0);
        return "";
    }

}
