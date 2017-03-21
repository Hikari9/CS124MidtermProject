package me.ricotiongson.dragonsms.modules;

import me.ricotiongson.dragonsms.session.SessionManager;
import me.ricotiongson.elegantsms.annotations.ArrayDelim;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.Priority;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class DragonModule extends SessionManager implements SmsModule {

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

    @SmsQuery("HINT")
    public String hint() {
        return ""; // TODO: hint here
    }

    @SmsQuery("GO <ROOM#>")
    public String go(String roomName) {
        return checkRoom(this.capitalize(roomName));
    }

    //    @SmsQuery("ROOM<#>")
    //    String gotoRoom(int roomNumber) {
    //        return gotoRoom("Room" + roomNumber);
    //    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("<COMMAND> <PARAMS...>")
    public String command(String command, @ArrayDelim(" ") String... params) {
        return processRoom(command, params);
    }

    //    @SmsQuery("EXIT")
    //    String exit() {
    //        System.exit(0);
    //        return "";
    //    }

    private String capitalize(String text) {
        if (text == null || text.length() == 0)
            return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
