package me.ricotiongson.dragonsms.modules;

import me.ricotiongson.elegantsms.annotations.BindByOrder;
import me.ricotiongson.elegantsms.annotations.CaseSensitive;
import me.ricotiongson.elegantsms.annotations.DispatchPriority;
import me.ricotiongson.elegantsms.annotations.SmsQuery;
import me.ricotiongson.elegantsms.framework.SmsModule;
import me.ricotiongson.elegantsms.dispatch.Priority;

@BindByOrder
public class NavigationModule implements SmsModule {

    @SmsQuery("REGISTER <NAME>")
    public String register(String name) {
        /* TODO
            - Store name in session
         */
        return "Hello, " + name + ", welcome to DragonSMS";
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
        return "<Reply based on room and gameState>";
    }

    @SmsQuery("HINT")
    public String command() {
        return "<Reply based on room and gameState>";
    }

}
