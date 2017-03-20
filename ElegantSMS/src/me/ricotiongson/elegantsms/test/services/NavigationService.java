package me.ricotiongson.elegantsms.test.services;

import me.ricotiongson.elegantsms.annotations.BindByOrder;
import me.ricotiongson.elegantsms.annotations.ArrayDelim;
import me.ricotiongson.elegantsms.annotations.CaseSensitive;
import me.ricotiongson.elegantsms.annotations.SMSQuery;
import me.ricotiongson.elegantsms.framework.SMSService;

@BindByOrder
public class NavigationService implements SMSService {

    @SMSQuery("REGISTER <NAME>")
    public String register(String name) {
        /* TODO
            - Store name in session
         */
        return "Hello, " + name + ", welcome to DragonSMS";
    }

    @SMSQuery("START")
    public String start() {
        /* TODO
            - Starts new session with name
            - Reset gameState and Room in session
         */
        return "<Room1 intro>";
    }

    @SMSQuery("GO <ROOM#>")
    public String go(String roomNumber) {
        /* TODO
            - Set Room in session to supplied room
            - Properly format the room string so it will be properly handled
            - Add room error check if non-existent with appropriate message
         */
        return "<Room# intro>";
    }

    @CaseSensitive
    @SMSQuery("<COMMAND> <PARAMS...>")
    public String command(String command, String... params) {
        /* TODO
            - Invoke the supplied command (with the params as needed) with current room and session
            - Returned gameState should be stored in session
         */
        return "<Reply based on room and gameState>";
    }

    @SMSQuery("HINT")
    public String command() {
        return "<Reply based on room and gameState>";
    }

}
