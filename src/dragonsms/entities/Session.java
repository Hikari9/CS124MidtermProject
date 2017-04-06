package dragonsms.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import room.GameState;

@Entity
@Table(name = "session", schema = "DragonSMS")
public class Session {

    @Id
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Basic
    @Column(name = "gameState", nullable = true)
    private int gameState;

    @Basic
    @Column(name = "room", nullable = false, columnDefinition = "varchar(255) default 'Room1'")
    private String room;

    public Session(String name) {
        this.name = name;
        gameState = 0;
        room = "Room1";
    }

    public Session() {
        /* required empty constructor for Hibernate */
    }

    /* GETTERS AND SETTERS */

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameState getGameState() {
        return new GameState(gameState);
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState.getState();
    }

    public Object getRoom() {
        try {
            return Class.forName("room." + room).newInstance();
        } catch (Exception ignore) {}
        return null;
    }

    public String getRoomName() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room.getClass().getSimpleName();
    }

    @Override
    public String toString() {
        return getName() + " @" + getRoomName() + " [gameState:" + gameState + "]";
    }

}
