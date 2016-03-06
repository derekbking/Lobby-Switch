package com.lobbyswitch.util;

import com.lobbyswitch.LobbySwitch;

/**
 * Created by derek on 3/6/2016.
 */
public class ChatUtil {

    public static String t(final String string, final String... values) {
        String message = LobbySwitch.p.getMessages().getString(string);
        int index = 0;
        while (message.contains("{" + index + "}")) {
            message = message.replace("{" + index + "}", values[index]).replace("§", "\247");
            index++;
        }
        return message;
    }
}
