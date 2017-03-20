package me.ricotiongson.dragonsms;

import java.util.Scanner;

import me.ricotiongson.dragonsms.modules.NavigationModule;
import me.ricotiongson.elegantsms.framework.SmsApplication;
import me.ricotiongson.elegantsms.framework.SmsPatternMismatchError;

public class Main {


    public static void main(String[] args) {

        // load application from package
        SmsApplication dragonSMS = SmsApplication.fromPackage("me.ricotiongson.dragonsms.modules");
        System.out.println(dragonSMS.dispatch("HINT"));
        // scan messages from console
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            try {
                String reply = dragonSMS.dispatch(input);
                System.out.println(reply);
            } catch (SmsPatternMismatchError e) {
                System.err.println(e.getMessage());
            }
        }

    }

}
