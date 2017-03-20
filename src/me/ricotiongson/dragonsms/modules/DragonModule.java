package me.ricotiongson.dragonsms.modules;

import java.lang.reflect.Method;

import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;

public class DragonModule implements SmsModule {

    @SmsQuery("REGISTER <NAME>\n")
    public String registerName(String name) {
        return "Hello " + name + ", welcome to DragonSMS";
    }

    @SmsQuery("HINT")
    public String hint() {
        StringBuilder sb = new StringBuilder();
        sb.append("Possible commands:\n");
        for (Method method : this.getClass().getDeclaredMethods()) {
            if (method.isAnnotationPresent(SmsQuery.class)) {
                sb.append("\t");
                sb.append(method.getDeclaredAnnotation(SmsQuery.class).value().trim());
                sb.append("\n");
            }
        }
        return sb.toString();
    }

}
