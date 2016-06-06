package com.lobbyswitch;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lobbyswitch.command.CommandManager;
import com.lobbyswitch.config.ConfigManager;
import com.lobbyswitch.config.ConfigUpdater;
import com.lobbyswitch.listeners.CypherInventoryListener;
import com.lobbyswitch.listeners.CypherPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:45 PM
 */
public class LobbySwitch extends JavaPlugin {

    public static LobbySwitch p;
    private FileConfiguration config;
    private FileConfiguration messages;
    private ConfigManager configManager;
    private CommandManager commandManager;
    private HashMap<String, ServerData> servers = new HashMap<>();
    private String pluginChannel = "BungeeCord";

    @Override
    public void onEnable() {
        p = this;
        loadConfig();
        configManager = new ConfigManager(config);
        commandManager = new CommandManager();
        registerListener(Bukkit.getPluginManager());
        if (getServer().getPluginManager().getPlugin("RedisBungee") != null) {
            pluginChannel = "RedisBungee";
        }
        loadMessages();

        Bukkit.getMessenger().registerOutgoingPluginChannel(this, pluginChannel);
        Bukkit.getMessenger().registerIncomingPluginChannel(this, pluginChannel, new CypherPlayerListener());

        if (!Bukkit.getOnlinePlayers().isEmpty()) {
            LobbySwitch.p.getServer().getScheduler().scheduleSyncDelayedTask(LobbySwitch.p, new Runnable() {
                @Override
                public void run() {
                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                    byteArrayDataOutput.writeUTF("GetServers");
                    Bukkit.getOnlinePlayers().iterator().next().sendPluginMessage(LobbySwitch.p, "BungeeCord", byteArrayDataOutput.toByteArray());
                }
            }, 20);
        }
    }

    public FileConfiguration getMessages() {
        return messages;
    }

    public FileConfiguration getFileConfig() {
        return config;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }

    public CommandManager getCommandManager() {
        return commandManager;
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

    private void loadMessages() {
        try {
            Reader messagesStream = new InputStreamReader(this.getResource("messages.yml"), "UTF8");
            messages = YamlConfiguration.loadConfiguration(messagesStream);
        } catch (UnsupportedEncodingException e) {
            System.out.println("An error has occurred while loading the plugin messages.");
        }
    }

    public String getPluginChannel() {
        return pluginChannel;
    }
}
