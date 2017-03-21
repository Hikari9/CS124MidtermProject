package me.ricotiongson.dragonsms.session;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;

import me.ricotiongson.elegantsms.util.TypeConverter;

/**
 * Class that manages session
 */
public class SessionManager {

    /**
     * Static session only visible to descendant modules
     */
    private static Session session;

    /**
     * Starts a new session with name
     * @param name
     */
    public void startSession(String name) {
        SessionManager.session = new Session(name);
    }

    /**
     * Gets the current session
     * @return
     */
    public Session getSession() {
        return session;
    }

    public String processRoom(String methodName, String... params) {
        Object room = getSession().getRoom();
        if (methodName == null)
            return "method not found";
        Method[] methodList = room.getClass().getDeclaredMethods();
        for (Method method : methodList) {
            method.setAccessible(true);
            // try out the method if it works
            if (method.getName().equalsIgnoreCase(methodName) && method.getParameterCount() == params.length + 1) {
                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[params.length + 1];
                args[0] = session.getGameState(); // first argument is always game state
                boolean found = true;
                // cast via type conversion
                for (int i = 0; i < params.length; ++i) {
                    try {
                        // wrong argument
                        args[i + 1] = TypeConverter.convertParameter(params[i], parameters[i + 1]);
                    } catch (Exception e) {
                        found = false;
                        break;
                    }
                }
                if (found) {
                    // we can invoke this method
                    try {
                        String reply = (String) method.invoke(room, args);
                        return reply;
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "method not found";
    }

    /**
     * Processes a room with command via the room command manager
     * @return
     */
    public String checkRoom(String roomName) {
        Object room;
        try {
            Constructor constructor = Class.forName("room." + roomName).getDeclaredConstructor();
            constructor.setAccessible(true);
            room = constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            return "room not found";
        }
        getSession().setRoom(room);
        return processRoom("checkRoom");
    }


}
