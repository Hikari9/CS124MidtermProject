package dragonsms.modules;

import com.elegantsms.annotations.ArrayDelim;
import com.elegantsms.annotations.DispatchPriority;
import com.elegantsms.annotations.RegexDebug;
import com.elegantsms.annotations.SmsQuery;
import com.elegantsms.framework.Priority;
import com.elegantsms.framework.SmsModule;

import dragonsms.session.SessionManager;

@RegexDebug
public class AdventureModule extends SessionManager implements SmsModule {

    @SmsQuery("GO <ROOM#>")
    public String go(String roomName) {
        return checkRoom(roomName);
    }

    @DispatchPriority(Priority.LOWEST)
    @SmsQuery("<COMMAND> <PARAMS...>")
    public String command(String command, @ArrayDelim("\\s+") String... params) {
        return processRoom(command, params);
    }

}
