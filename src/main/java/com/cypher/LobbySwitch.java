package com.cypher;

import com.cypher.listeners.CypherInventoryListener;
import com.cypher.listeners.CypherPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:45 PM
 */
public class LobbySwitch extends JavaPlugin {

    public static LobbySwitch p;
    private FileConfiguration config;

    @Override
    public void onEnable() {
        p = this;
        loadConfig();
        registerListener(Bukkit.getPluginManager());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        Bukkit.getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new CypherPlayerListener());
        CommandManager.registerCommands();
    }

    public FileConfiguration getFileConfig() {
        return config;
    }

    private void registerListener(PluginManager pluginManager) {
        HandlerList.unregisterAll(this);

        final CypherInventoryListener inventoryListener = new CypherInventoryListener();
        final CypherPlayerListener playerListener = new CypherPlayerListener();

        pluginManager.registerEvents(inventoryListener, this);
        pluginManager.registerEvents(playerListener, this);
    }

    private void loadConfig() {
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        config = this.getConfig();
        this.saveConfig();
    }
}
