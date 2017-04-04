package dragonsms.entities;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "session", schema = "DragonSMS", catalog = "")
public class SessionEntity {
    private int id;
    private String name;
    private Integer gameState;

    @Id
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 255)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "gameState", nullable = true)
    public Integer getGameState() {
        return gameState;
    }

    public void setGameState(Integer gameState) {
        this.gameState = gameState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SessionEntity that = (SessionEntity) o;

        if (id != that.id) return false;
        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (gameState != null ? !gameState.equals(that.gameState) : that.gameState != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (gameState != null ? gameState.hashCode() : 0);
        return result;
    }
}
