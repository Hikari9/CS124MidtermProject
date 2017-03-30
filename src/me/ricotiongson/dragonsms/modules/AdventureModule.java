package me.ricotiongson.dragonsms.modules;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import me.ricotiongson.dragonsms.session.Session;
import me.ricotiongson.dragonsms.session.SessionManager;
import me.ricotiongson.elegantsms.annotations.ArrayDelim;
import me.ricotiongson.elegantsms.annotations.RegexDebug;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.Priority;
import me.ricotiongson.elegantsms.framework.SmsModule;

@RegexDebug(false)
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

    @DispatchPriority(Priority.LOWEST)
    @SmsQuery("EXIT")
    String exit() {
        System.exit(0);
        return "";
    }

}
