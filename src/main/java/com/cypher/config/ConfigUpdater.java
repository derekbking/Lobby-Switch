package com.cypher.config;

import com.cypher.LobbySwitch;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;

/**
 * Created by Derek on 8/9/2014.
 * Time: 1:25 PM
 */
public class ConfigUpdater {

    private FileConfiguration fileConfiguration;
    private String fromVersion;

    public ConfigUpdater(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public void update() {
        if (fileConfiguration.get("Version").equals("DEFAULT")) {
            if (!fileConfiguration.getList("Servers").isEmpty()) {
                ArrayList<String> arrayList = (ArrayList) fileConfiguration.getList("Servers");
                String string = arrayList.get(0);
                String[] split = string.split(":");

                if (split.length == 5) {
                    fromVersion = "0.2.1";
                } else if (split.length == 6) {
                    fromVersion = "0.2.2";
                } else {
                    fromVersion = "Unknown";
                }
            } else {
                fromVersion = "0.2.1";
            }
        } else {
            fromVersion = fileConfiguration.getString("Version");
        }

        if (fromVersion.equals("0.2.1")) {
            ArrayList<String> newServerList = new ArrayList<String>();
            for (int i = 0; i < fileConfiguration.getList("Servers").size(); i++) {
                String string = fileConfiguration.getStringList("Servers").get(i);
                String split = string + ":" + (i + 1);
                newServerList.add(split);
            }
            fileConfiguration.set("Servers", newServerList);
        }
        fileConfiguration.set("Version", LobbySwitch.p.getDescription().getVersion());
        LobbySwitch.p.saveConfig();
    }
}
