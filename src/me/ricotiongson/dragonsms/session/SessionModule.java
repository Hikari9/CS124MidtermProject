package me.ricotiongson.dragonsms.session;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;

import me.ricotiongson.elegantsms.util.TypeConverter;

public class SessionModule {

    /**
     * Static session only visible to descendant modules
     */
    private static Session session;

    /**
     * Starts a new session with name
     * @param name
     */
    public void startSession(String name) {
        SessionModule.session = new Session(name);
    }

    /**
     * Gets the current session
     * @return
     */
    protected Session getSession() {
        return session;
    }

    /**
     * Processes a room with command via the room command manager
     * @return
     */
    protected String processRoom(String roomName, String methodName, String... params) {
        Session session = getSession();
        Object room;
        try {
            room = Class.forName("room." + roomName).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
            return "room not found";
        }
        if (methodName == null)
            return "method not found";
        Method[] methodList = room.getClass().getDeclaredMethods();
        for (Method method : methodList) {
            // try out the method if it works
            if (method.getName().equals(methodName) && method.getParameterCount() == params.length + 1) {

                Parameter[] parameters = method.getParameters();
                Object[] args = new Object[params.length + 1];
                args[0] = session.getGameState(); // first argument is always game state
                boolean found = true;
                // cast via type conversion
                for (int i = 0; i < params.length; ++i) {
                    try {
                        // wrong argument
                        args[i] = TypeConverter.convertParameter(params[i], parameters[i + 1]);
                    } catch (Exception e) {
                        found = false;
                    }
                }
                if (found) {
                    // we can invoke this method
                    try {
                        return (String) method.invoke(room, args);
                    } catch (IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return "method not found";
    }


}
