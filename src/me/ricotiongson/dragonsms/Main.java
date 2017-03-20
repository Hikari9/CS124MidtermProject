package me.ricotiongson.dragonsms;

import me.ricotiongson.dragonsms.modules.NavigationModule;
import me.ricotiongson.elegantsms.framework.SmsApplication;

public class Main {

    public static void main(String[] args) {

        SmsApplication app = SmsApplication.fromPackage("me.ricotiongson.dragonsms.modules");
        System.out.println(app.dispatch("register rico / 10"));
    }

}
