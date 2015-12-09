package com.lobbyswitch.versions;

import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by derek on 12/9/2015.
 */
public abstract class Config0_5_3 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        fileConfiguration.set(ConfigPaths.ADD_ON_JOIN, true);
        fileConfiguration.set(ConfigPaths.SELECTOR_DROPPABLE, false);
        fileConfiguration.set(ConfigPaths.SELECTOR_SLOT_FORCED, false);
        fileConfiguration.set(ConfigPaths.SELECTOR_SLOT_POSITION, -1);
        fileConfiguration.set(ConfigPaths.VERSION, "0.5.3");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.3")){
                return true;
            }
        }
        return false;
    }
}
