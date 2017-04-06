package room;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Room1 {
    public String checkRoom(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("You find yourself inside a dark room.  You see four doors marked Room2, Room3, Room4");
        pw.println("use these as URLs to enter.");
        pw.println();

        if (state.is(GameState.WORD_FOUND_2)
            && state.is(GameState.WORD_FOUND_3)
            && state.is(GameState.WORD_FOUND_4)) {
            pw.println("You may now access secret Room5");
        }

        return sw.toString();
    }
}
