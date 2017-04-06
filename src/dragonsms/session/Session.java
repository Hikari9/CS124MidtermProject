package dragonsms.session;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import room.GameState;
import room.Room1;

@Entity
@Table(name = "session", schema = "DragonSMS")
public class Session {

    @Id
    @Column(name = "name", nullable = false, unique = true, length = 255)
    private String name;

    @Basic
    @Column(name = "gameState", nullable = true)
    private int gameState;

    @Transient
    private Object room;

    public Session(String name) {
        this.name = name;
        gameState = 0;
        room = new Room1();
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
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Session that = (Session) o;

        if (getName().equals(that.getName())) return false;
        return gameState == that.gameState;
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + Integer.hashCode(gameState);
        return result;
    }

    @Override
    public String toString() {
        return getName() + " [gameState:" + gameState + "]";
    }

}
