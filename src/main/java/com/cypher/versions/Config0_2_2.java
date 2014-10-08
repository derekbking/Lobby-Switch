package com.cypher.versions;

import com.cypher.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

/**
 * Created by Derek on 8/16/2014.
 * Time: 12:00 AM
 */
public abstract class Config0_2_2 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        ArrayList<String> newServerList = new ArrayList<String>();
        for (int i = 0; i < fileConfiguration.getList(ConfigPaths.OLD_SERVERS).size(); i++) {
            String string = fileConfiguration.getStringList(ConfigPaths.OLD_SERVERS).get(i);
            String split = string + ":" + (i + 1);
            newServerList.add(split);
        }
        fileConfiguration.set(ConfigPaths.OLD_SERVERS, newServerList);
        fileConfiguration.set(ConfigPaths.VERSION, "0.2.2");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.2.2") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.2.3") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.3")) {
                return true;
            }
        }
        return false;
    }
}
