package dragonsms.modules;

import dragonsms.session.SessionManager;
import com.elegantsms.annotations.RegexDebug;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.SmsModule;

@RegexDebug(false)
public class RegistrationModule extends SessionManager implements SmsModule {

    private String sessionId; // stores the name of user in the session
    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        this.sessionId = name;
        return "Hello, " + name + ", welcome to DragonSMS";
    }

    @SmsQuery("START")
    public String start() {
        startSession(sessionId); // starts the session
        return checkRoom("Room1"); // checkout Room1
    }
}
