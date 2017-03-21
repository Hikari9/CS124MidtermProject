package me.ricotiongson.dragonsms.modules;

import java.util.Arrays;

import me.ricotiongson.dragonsms.session.SessionManager;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.Priority;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class AdventureModule extends SessionManager implements SmsModule {

    @SmsQuery("GO <ROOM#>")
    String go(String roomName) {
        return checkRoom(this.capitalize(roomName));
    }

//    @SmsQuery("ROOM<#>")
//    String gotoRoom(int roomNumber) {
//        return gotoRoom("Room" + roomNumber);
//    }

    @DispatchPriority(Priority.LOWEST + 1)
    @SmsQuery("<COMMAND> <PARAMS...>")
    String command(String command, String... params) {
        return processRoom(command, params);
    }

//    @SmsQuery("EXIT")
//    String exit() {
//        System.exit(0);
//        return "";
//    }

    public String capitalize(String text) {
        if (text == null || text.length() == 0)
            return "";
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
