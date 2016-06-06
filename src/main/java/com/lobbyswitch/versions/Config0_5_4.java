package com.lobbyswitch.versions;

import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by derek on 3/6/2016.
 */
public abstract class Config0_5_4 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        for (String string : fileConfiguration.getConfigurationSection(ConfigPaths.SERVER_SLOTS).getKeys(false)) {
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_ENCHANTED, Integer.valueOf(string)), false);
        }
        fileConfiguration.set(ConfigPaths.VERSION, "0.5.4");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.4") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.5") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.6")){
                return true;
            }
        }
        return false;
    }
}
