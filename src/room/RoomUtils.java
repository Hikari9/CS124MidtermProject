package room;

import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

@Component
public class RoomUtils {

    // ===== NO NEED TO WORRY ABOUT WHAT IS PAST HERE ====
    // ===== THIS SIMPLY GIVES A DEMO OF REFLECTION   =====


    public HashMap<String, Object> processRoom(String room, Integer gameState, String command) {
        try {
            Object roomObj = Class.forName("app.rooms." + room).newInstance();

            GameState gs = new GameState();
            if (gameState != null) {
                gs = new GameState(gameState);
            }


            String reply = "bad command: " + command;
            if (command != null) {
                String[] commandData = command.split(" ");


                int size = 1 + (commandData.length - 1);

                Class[] paramClassList = new Class[size];
                paramClassList[0] = GameState.class;

                Object[] argumentList = new Object[size];
                argumentList[0] = gs;

                if (size == 2) {
                    try {
                        int temp = Integer.parseInt(commandData[1]);
                        paramClassList[1] = Integer.class;
                        argumentList[1] = temp;
                    } catch (Exception e) {
                        paramClassList[1] = String.class;
                        argumentList[1] = commandData[1];
                    }
                } else if (size > 2) {
                    throw new RuntimeException("Too many command parameters");
                }

                Method m = roomObj.getClass().getDeclaredMethod(commandData[0], paramClassList);
                reply = (String) m.invoke(roomObj, argumentList);

            } else {
                // no command
                throw new RuntimeException("Command not found");
            }


            HashMap<String, Object> replyMap = new HashMap<String, Object>();
            replyMap.put("status", gs.getState());
            replyMap.put("message", reply);

            return replyMap;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            HashMap<String, Object> replyMap = new HashMap<String, Object>();
            replyMap.put("message", e.getMessage() + " : " + e.getClass().getName());
            return replyMap;
        } catch (Exception e) {
            e.printStackTrace();
            HashMap<String, Object> replyMap = new HashMap<String, Object>();
            replyMap.put("message", e.getMessage());
            return replyMap;
        }
    }
}
