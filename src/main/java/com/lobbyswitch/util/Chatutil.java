package com.lobbyswitch.util;

import com.lobbyswitch.LobbySwitch;

/**
 * Created by derek on 3/6/2016.
 */
public class Chatutil {

    public static String t(final String string, final String... values) {
        String message = LobbySwitch.p.getMessages().getString(string);
        int index = 0;
        while (message.contains("{" + index + "}")) {
            message = message.replace("{" + index + "}", values[index]).replace("§", "\247");
            index++;
        }
        return message;
    }

    public static String replaceLowerCasePlaceholder(String text, String toReplace, Object replaceWith) {
        int startIndex = text.toLowerCase().indexOf(toReplace.toLowerCase());
        int endIndex = startIndex + toReplace.length();

        return startIndex == -1 ? text : text.substring(0, startIndex) + replaceWith + text.substring(endIndex, text.length());
    }
}
