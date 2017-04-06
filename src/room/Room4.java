package room;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Room4 {
    static boolean wordFound;

    public String checkRoom(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        pw.println("You find yourself in an empty circular room.  On the wall opposite, you see '342 * 587 = _' ");
        pw.println("You can say the 'answer (number)' no parenthesis");
        pw.println("You can 'look' around");
        pw.println();

        if (state.is(GameState.WORD_FOUND_2)
            && state.is(GameState.WORD_FOUND_3)
            && state.is(GameState.WORD_FOUND_4)) {
            pw.println("You may now access secret Room5");
        }

        return sw.toString();
    }

    public String look(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        pw.println("You see nothing else of interest.");

        return sw.toString();
    }

    public String answer(GameState state, Integer ans) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (ans == 342 * 587) {
            state.set(GameState.WORD_FOUND_4);

            pw.println("A low voice reverberates the word 'Ka' and fades away");

            if (state.is(GameState.WORD_FOUND_2)
                && state.is(GameState.WORD_FOUND_3)
                && state.is(GameState.WORD_FOUND_4)) {
                pw.println("You may now access secret Room5");
            }

        } else {
            pw.println("The door closes behind you and you are trapped here forever to contemplate the value of 342*587... The End");
            state.set(GameState.DEAD);
        }

        return sw.toString();
    }
}
