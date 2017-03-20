package me.ricotiongson.dragonsms.modules;

import java.util.Arrays;

import me.ricotiongson.elegantsms.annotations.CaseSensitive;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;
import me.ricotiongson.elegantsms.framework.Priority;

public class NavigationModule /*implements SmsModule*/ {

    @SmsQuery("REGISTER <NAME>/<AGE>")
    public String register(String name, int age) {
        /* TODO
            - Store name in session
         */
        return "Hello, " + name + ":" + age + ", welcome to DragonSMS";
    }

    @SmsQuery("START")
    public String start() {
        /* TODO
            - Starts new session with name
            - Reset gameState and Room in session
         */
        return "<Room1 intro>";
    }

    @SmsQuery("GO <ROOM#>")
    public String go(String roomNumber) {
        /* TODO
            - Set Room in session to supplied room
            - Properly format the room string so it will be properly handled
            - Add room error check if non-existent with appropriate message
         */
        return "<Room# intro>";
    }

    @DispatchPriority(Priority.LOWEST)
    @CaseSensitive
    @SmsQuery("<COMMAND> <PARAMS...>")
    public String command(String command, String... params) {
        /* TODO
            - Invoke the supplied command (with the params as needed) with current room and session
            - Returned gameState should be stored in session
         */
        System.out.println(command + ": " + Arrays.toString(params));
        return "<Reply based on room and gameState>";
    }

    @SmsQuery("HINT")
    public String command() {
        return "<Reply based on room and gameState>";
    }

}
