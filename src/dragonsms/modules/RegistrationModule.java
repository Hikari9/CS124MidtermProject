package dragonsms.modules;

import com.elegantsms.annotations.DispatchPriority;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.Priority;
import com.elegantsms.framework.SmsModule;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import dragonsms.session.Session;
import dragonsms.session.SessionManager;

//@RegexDebug
public class RegistrationModule extends SessionManager implements SmsModule {

    private String sessionId; // stores the name of user in the session

    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        endSession();
        this.sessionId = name;
        // check if user already exists
        if (getDao().findOne(name) == null)
            return "Hello, " + name + ", welcome to DragonSMS. Send START to play.";
        else
            return "Welcome back, " + name + ". Send CONTINUE to resume from your previous game, or START to start a new game.";
    }

    @SmsQuery("CONTINUE")
    public String continueSession() {
        if (sessionId == null || getDao().findOne(sessionId) == null)
            return "Invalid command, have you registered or started a session before?";
        startSession(sessionId);
        sessionId = null;
        return checkRoom(getSession().getRoomName());
    }

    @SmsQuery("START")
    public String start() {
        if (sessionId == null)
            return "Cannot start session. Make sure you register your name first.";
        if (getDao().exists(sessionId))
            getDao().delete(sessionId);
        startSession(sessionId); // starts new session
        sessionId = null;
        return checkRoom(getSession().getRoomName()); // checkout Room1
    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("END")
    String end() {
        String sessionName = getSession().toString();
        endSession();
        return "Ended session: " + sessionName;
    }

    @SmsQuery("HINT")
    public String hint() {

        StringBuilder sb = new StringBuilder();

        sb.append("List of commands:\n")
            .append("\tHINT               - shows list of commands\n")
            .append("\tREGISTER <NAME>    - registers your name for the session\n");

        if (sessionId != null)

        sb
            .append("\tSTART              - starts a new session and goes to the first room\n")
            .append("\tCONTINUE           - continues from a previous session\n");

        if (getSession() != null)
            sb.append("\tEND                - ends the current session"\n);

        sb.append("\tEXIT               - exits the application\n");


        Session session = getSession();
        if (session != null && session.getRoom() != null) {
            sb.append("\tGO <Room#>         - checks out a room (e.g. GO Room2)\n")
                .append("\n")
                .append("Room specific commands:\n");
            Object room = session.getRoom();
            for (Method method : room.getClass().getDeclaredMethods()) {
                if (method.getName().equals("checkRoom"))
                    continue;
                // get parameters
                sb.append("\t").append(method.getName());
                Parameter[] parameters = method.getParameters();
                for (int i = 1; i < parameters.length; ++i) {
                    sb.append(" <").append(parameters[i].getName()).append(">");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }
        return sb.toString();

    }
}
