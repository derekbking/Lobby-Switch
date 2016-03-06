package com.lobbyswitch.config;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.versions.*;
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
        if (Config0_3_8.equals(fileConfiguration)) {
            fileConfiguration = Config0_4.update(fileConfiguration);
        }
        if(Config0_4.equals(fileConfiguration)) {
            fileConfiguration = Config0_4_9.update(fileConfiguration);
        }
        if (Config0_4_9.equals(fileConfiguration)) {
            fileConfiguration = Config0_5_3.update(fileConfiguration);
        }
        if (Config0_5_3.equals(fileConfiguration)) {
            fileConfiguration = Config0_5_4.update(fileConfiguration);
        }
        LobbySwitch.p.saveConfig();
    }
}
