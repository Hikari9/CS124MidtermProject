package me.ricotiongson.dragonsms.session;

import java.util.HashMap;

import room.GameState;
import room.Room1;
import room.RoomCommandManager;

public class Session {
    private String name;
    private GameState gameState;
    private Object room;

    public Session(String name) {
        this.name = name;
        gameState = new GameState(0);
        room = new Room1();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Object getRoom() {
        return room;
    }

    public void setRoom(Object room) {
        this.room = room;
    }
}
