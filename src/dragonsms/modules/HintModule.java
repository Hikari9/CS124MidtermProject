package dragonsms.modules;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import dragonsms.session.Session;
import dragonsms.session.SessionManager;
import com.elegantsms.annotations.RegexDebug;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.SmsModule;

//@RegexDebug
public class HintModule extends SessionManager implements SmsModule {

    @SmsQuery("HINT")
    public String hint() {

        StringBuilder sb = new StringBuilder();

        sb.append("List of commands:\n")
            .append("\tHINT               - shows list of commands\n")
            .append("\tREGISTER <NAME>    - registers your name for the session\n")
            .append("\tSTART              - starts a new session and goes to the first room\n")
            .append("\tEXIT               - exit\n");


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
