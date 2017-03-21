package me.ricotiongson.dragonsms;

import java.util.Scanner;

import me.ricotiongson.elegantsms.framework.SmsApplication;
import me.ricotiongson.elegantsms.framework.SmsPatternMismatchException;

public class Main {

    // load application from package
    static SmsApplication dragonSMS = SmsApplication.fromPackage("me.ricotiongson.dragonsms.modules");

    public static void main(String[] args) {

        // scan messages from console
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            try {
                String reply = dragonSMS.dispatch(input);
                System.out.println(reply);
            } catch (SmsPatternMismatchException e) {
                e.printStackTrace();
            }
        }

    }

}
