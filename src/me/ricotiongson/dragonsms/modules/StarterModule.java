package me.ricotiongson.dragonsms.modules;

import me.ricotiongson.dragonsms.session.Session;
import me.ricotiongson.dragonsms.session.SessionModule;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;
import room.Room1;

public class StarterModule extends SessionModule implements SmsModule {

    private String sessionId;

    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        this.sessionId = name;
        return "Hello, " + name + ", welcome to DragonSMS";
    }

    @SmsQuery("START")
    public String start() {
        startSession(sessionId);
        return processRoom("Room1", "checkRoom");
    }

}
