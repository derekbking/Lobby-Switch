package com.cypher.config;

import com.cypher.LobbySwitch;
import com.cypher.versions.Config0_2_1;
import com.cypher.versions.Config0_2_2;
import com.cypher.versions.Config0_3_5;
import com.cypher.versions.Config0_3_8;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Created by Derek on 8/9/2014.
 * Time: 1:25 PM
 */
public class ConfigUpdater {

    private FileConfiguration fileConfiguration;

    public ConfigUpdater(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public void update() {
        if (Config0_2_1.equals(fileConfiguration)) {
            fileConfiguration = Config0_2_2.update(fileConfiguration);
        }
        if (Config0_2_2.equals(fileConfiguration)) {
            fileConfiguration = Config0_3_5.update(fileConfiguration);
        }
        if (Config0_3_5.equals(fileConfiguration)) {
            fileConfiguration = Config0_3_8.update(fileConfiguration);
        }
        LobbySwitch.p.saveConfig();
    }
}
