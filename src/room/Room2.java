package room;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Room2 {

    public String checkRoom(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        pw.println("You the door leads down some steps into an underground cave system. There is a deep pool in the middle of the cave.");
        pw.println("You see something shiny at the bottom of the pool.");
        pw.println("You can command to 'swim' in the pool.");
        pw.println("You can command to 'look' around.");
        pw.println();

        if (state.is(GameState.WORD_FOUND_2)
            && state.is(GameState.WORD_FOUND_3)
            && state.is(GameState.WORD_FOUND_4)) {
            pw.println("You may now access secret Room5");
        }

        return sw.toString();
    }

    public String swim(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        state.set(GameState.IN_POOL);

        pw.println("You find a shiny sword at the bottom.");
        pw.println("You can command to 'takeSword'");

        return sw.toString();
    }

    public String takeSword(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (state.is(GameState.IN_POOL)) {
            state.set(GameState.TOOK_SWORD);
            pw.println("You take the bright shiny sword.");
        } else {
            pw.println("What sword?");
        }

        return sw.toString();
    }

    public String look(GameState state) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (!state.is(GameState.GRAVE_FOUND)) {
            state.set(GameState.GRAVE_FOUND);
            pw.println("You find a pile rubble.  It looks like a shallow grave.");
            pw.println("You can command to 'dig' to see what is under it.");
        } else {
            pw.println("You see nothing else of interest.");
        }

        return sw.toString();
    }

    public String dig(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (!state.is(GameState.GRAVE_FOUND)) {
            pw.println("You dig into the ground and disturb the home of a poisonous snake.  It bites and you die... The End");
            state.set(GameState.DEAD);
        } else {
            state.set(GameState.WORD_FOUND_2);
            pw.println("You dig up the grave and find a skeleton holding a scroll.  It contains 3 words but 2 are unreadable.  The remaining word says 'Zam'");

            if (state.is(GameState.WORD_FOUND_2)
                && state.is(GameState.WORD_FOUND_3)
                && state.is(GameState.WORD_FOUND_4)) {
                pw.println("You may now access secret Room5");
            }
        }

        return sw.toString();
    }
}
