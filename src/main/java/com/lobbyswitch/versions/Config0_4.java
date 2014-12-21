package com.lobbyswitch.versions;

import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Derek on 11/15/2014.
 * Time: 9:23 PM
 */
public abstract class Config0_4 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        for (String string : fileConfiguration.getConfigurationSection(ConfigPaths.SERVER_SLOTS).getKeys(false)) {
            List<String> lore = Arrays.asList("&7%PLAYER_COUNT% &cOnline");
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_LORE, Integer.valueOf(string)), lore);
        }
        fileConfiguration.set(ConfigPaths.VERSION, "0.4");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.1") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.2") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.3") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.4") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.5") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.6") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.7") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.8") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.9")) {
                return true;
            }
        }
        return false;
    }
}
