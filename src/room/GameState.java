package room;

public class GameState {
    // there are 8 states to track

    // rm2

    // 1 - inPool
    // 2 - graveFound
    // 4 - tookSword
    // 8 - wordFound_rm2
    // 16 - babyDead
    // 32 - chestFound
    // 64 - wordFound_rm3
    // 128 - wordFound_rm4

    public static final int IN_POOL = 1;
    public static final int GRAVE_FOUND = 2;
    public static final int TOOK_SWORD = 4;
    public static final int WORD_FOUND_2 = 8;
    public static final int BABY_DEAD = 16;
    public static final int CHEST_FOUND = 32;
    public static final int WORD_FOUND_3 = 64;
    public static final int WORD_FOUND_4 = 128;
    public static final int DEAD = 256;
    private int state = 0;

    public GameState() {

    }

    public GameState(int i) {
        state = i;
    }

    public boolean is(int constantName) {
        return ((state & constantName) == constantName);
    }

    public void set(int contantName) {
        state = state | contantName;
    }

    public int getState() {
        return state;
    }


}
