package com.lobbyswitch;

import com.lobbyswitch.config.ConfigManager;
import com.lobbyswitch.config.ConfigUpdater;
import com.lobbyswitch.listeners.CypherInventoryListener;
import com.lobbyswitch.listeners.CypherPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:45 PM
 */
public class LobbySwitch extends JavaPlugin {

    public static LobbySwitch p;
    private FileConfiguration config;
    private ConfigManager configManager;
    private HashMap<String, ServerData> servers = new HashMap<>();
    private String pluginChannel = "BungeeCord";

    @Override
    public void onEnable() {
        p = this;
        loadConfig();
        configManager = new ConfigManager(config);
        registerListener(Bukkit.getPluginManager());
        if (getServer().getPluginManager().getPlugin("RedisBungee") != null) {
            pluginChannel = "RedisBungee";
        }
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, pluginChannel);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, pluginChannel, new CypherPlayerListener());
        CommandManager.registerCommands();
    }

    public FileConfiguration getFileConfig() {
        return config;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public HashMap<String, ServerData> getServers() {
        return servers;
    }

    public void addServer(String server) {
        if (!servers.containsKey(server)) {
            ServerData serverData = new ServerData(server);
            Bukkit.getMessenger().registerIncomingPluginChannel(this, pluginChannel, serverData);
            servers.put(server, serverData);
        }
    }

    private void registerListener(PluginManager pluginManager) {
        HandlerList.unregisterAll(this);

        final CypherInventoryListener inventoryListener = new CypherInventoryListener();
        final CypherPlayerListener playerListener = new CypherPlayerListener();

        pluginManager.registerEvents(inventoryListener, this);
        pluginManager.registerEvents(playerListener, this);
    }

    private void loadConfig() {
        new ConfigUpdater(getConfig()).update();
        getConfig().options().copyDefaults(true);
        saveConfig();
        config = getConfig();
    }

    public String getPluginChannel() {
        return pluginChannel;
    }
}
