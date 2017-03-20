package me.ricotiongson.dragonsms.modules;

import me.ricotiongson.dragonsms.session.Session;
import me.ricotiongson.dragonsms.session.SessionModule;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class AdventureModule extends SessionModule implements SmsModule {

    @SmsQuery("GO <ROOM#>")
    public String go(String roomName) throws IllegalAccessException, InstantiationException {
        roomName = capitalize(roomName);
        return processRoom(roomName, "checkRoom");
    }

    public String capitalize(String text) {
        return text.substring(0, 1).toUpperCase() + text.substring(1).toLowerCase();
    }

}
