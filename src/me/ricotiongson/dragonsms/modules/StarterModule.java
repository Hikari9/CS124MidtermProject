package me.ricotiongson.dragonsms.modules;

import me.ricotiongson.dragonsms.session.SessionManager;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class StarterModule extends SessionManager implements SmsModule {

    private String sessionId;

    @SmsQuery("REGISTER <NAME>\n")
    public String register(String name) {
        this.sessionId = name;
        return "Hello, " + name + ", welcome to DragonSMS";
    }

    @SmsQuery("START")
    public String start() {
        startSession(sessionId);
        return checkRoom("Room1");
    }

}
