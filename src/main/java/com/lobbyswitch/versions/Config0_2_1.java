package com.lobbyswitch.versions;

import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Derek on 8/15/2014.
 * Time: 11:56 PM
 */
public abstract class Config0_2_1 {

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.OLD_INVENTORY_NAME) &&
                fileConfiguration.contains(ConfigPaths.OLD_INVENTORY_ROWS) &&
                fileConfiguration.contains(ConfigPaths.OLD_ITEMSTACK) &&
                fileConfiguration.contains(ConfigPaths.OLD_SERVERS) &&
                fileConfiguration.contains(ConfigPaths.VERSION)) {
            boolean equalsVersion = false;
            for (String string : fileConfiguration.getStringList(ConfigPaths.OLD_SERVERS)) {
                String[] split = string.split(":");

                if (split.length == 5) {
                    equalsVersion = true;
                } else {
                    equalsVersion = false;
                }
            }
            return equalsVersion;
        } else {
            return false;
        }
    }
}
