package room;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Room5 {
    public String checkRoom(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (state.is(GameState.WORD_FOUND_2)
            && state.is(GameState.WORD_FOUND_3)
            && state.is(GameState.WORD_FOUND_4)) {
            pw.println("You enter a long tunnel which opens into a large chamber.");
            pw.println("You can see an opening to the outside on the other side.");
            pw.println("As you walk towards it, a large dragon head peers from the opening.");
            pw.println("'What is the passphrase?' it asks.");
            pw.println("You can say the 'passphrase String'");
            pw.println("You can 'attack' the dragon.");
            pw.println("You can 'look' around");
            pw.println();
        } else {
            pw.println("You are not allowed in this room.  A ball of fire turns you to ash...");
            state.set(GameState.DEAD);
        }

        return sw.toString();
    }

    public String attack(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (state.is(GameState.TOOK_SWORD)) {
            pw.println("You charge to attack the dragon brandishing your sword.  The dragon breathes fire into the chamber turning you to ash... The End.");
            state.set(GameState.DEAD);
        } else {
            pw.println("In a flash of wisdom, you resist.  Only a fool would attack such a creature with his bare hands.");
        }

        return sw.toString();
    }

    public String passphrase(GameState state, String word) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if ((word.equalsIgnoreCase("AlaKaZam"))) {
            if (state.is(GameState.BABY_DEAD)) {
                pw.println("That is correct.  The dragon breathes fire into the chamber turning you to ash for killing her baby... The End.");
                state.set(GameState.DEAD);
            } else {
                pw.println("That is correct.  The dragon allows you to pass and you escape... Congratulations on your 10pts.");
                state.set(GameState.DEAD);
            }
        } else {
            pw.println("That is incorrect.  The dragon breathes fire into the chamber turning you to ash... The End.");
            state.set(GameState.DEAD);
        }

        return sw.toString();
    }

    public String look(GameState state) {

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        pw.println("There is no way around the dragon.");


        return sw.toString();
    }
}
