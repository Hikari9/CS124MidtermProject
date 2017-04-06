package room;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Room3 {
    public String checkRoom(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        pw.println("You enter a large cavern and hear deep laboured breathing.");
        pw.println("In the center of the chamber is small baby dragon sleeping on a big pile of gold coins.");
        pw.println("You can 'attack' the dragon.");
        pw.println("You can 'look' around.");
        pw.println();

        if (state.is(GameState.WORD_FOUND_2)
            && state.is(GameState.WORD_FOUND_3)
            && state.is(GameState.WORD_FOUND_4)) {
            pw.println("You may now access secret Room5");
        }

        return sw.toString();
    }

    public String attack(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        if (state.is(GameState.TOOK_SWORD)) {
            state.set(GameState.BABY_DEAD);
            pw.println("You charge the baby dragon with your bright shiny sword.  You cleave its head clean off.");
            pw.println("You can 'look' around.");
        } else {
            pw.println("You charge the baby dragon and try to take in on with your bare hands.  Its wakes and bites your head clean off... The End");
            state.set(GameState.DEAD);
        }

        return sw.toString();
    }


    public String look(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        if (!state.is(GameState.CHEST_FOUND)) {
            state.set(GameState.CHEST_FOUND);

            if (!state.is(GameState.BABY_DEAD)) {
                pw.println("You quietly avoid the baby dragon and make your way to the other side of the chamber and find a chest.");
                pw.println("You can 'openChest'");
            } else {
                pw.println("You make your way to the other side of the chamber and find a chest.");
                pw.println("You can 'openChest'");
            }
        } else {
            if (!state.is(GameState.BABY_DEAD)) {
                pw.println("Other than the sleeping baby dragon.  There is nothing of interest.");
            } else {
                pw.println("There is nothing of interest.");
            }
        }

        return sw.toString();
    }

    public String openChest(GameState state) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);


        if (state.is(GameState.CHEST_FOUND)) {
            state.set(GameState.WORD_FOUND_3);
            pw.println("Inside is a book.  A page is ear-marked and the word 'Ala' written in blood.");

            if (state.is(GameState.WORD_FOUND_2)
                && state.is(GameState.WORD_FOUND_3)
                && state.is(GameState.WORD_FOUND_4)) {
                pw.println("You may now access secret Room5");
            }

        } else {
            pw.println("What chest?");
        }

        return sw.toString();
    }
}
