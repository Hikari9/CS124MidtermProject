package me.ricotiongson.dragonsms;

import java.util.Scanner;

import me.ricotiongson.dragonsms.modules.DragonModule;
import me.ricotiongson.elegantsms.framework.SmsApplication;
import me.ricotiongson.elegantsms.framework.SmsPatternMismatchException;

public class Main {

    // Driver program
    public static void main(String[] args) {

        SmsApplication dragonSMS = SmsApplication.loadModules(DragonModule.class);
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            try {
                String reply = dragonSMS.getReply(sc.nextLine());
                System.out.println(reply);
            } catch (SmsPatternMismatchException e) {
                e.printStackTrace();
                System.out.println("invalid command");
            }
        }

    }

}
