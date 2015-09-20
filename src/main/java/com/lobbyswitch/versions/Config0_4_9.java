package com.lobbyswitch.versions;

import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Derek on 12/20/2014.
 * Time: 4:37 PM
 */
public abstract class Config0_4_9 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        fileConfiguration.set(ConfigPaths.MOTD_REFRESH_RATE, fileConfiguration.getInt(ConfigPaths.LEGACY_LORE_REFRESH_RATE));
        fileConfiguration.set(ConfigPaths.LEGACY_LORE_REFRESH_RATE, null);
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.4.9") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.1") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.5.2")){
                return true;
            }
        }
        return false;
    }
}
