package com.cypher;

import com.cypher.listener.CypherInventoryListener;
import com.cypher.listener.CypherPlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Derek on 8/5/2014.
 */
public class LobbySwitch extends JavaPlugin {

    public static LobbySwitch p;

    @Override
    public void onEnable() {
        p = this;
        registerListener(Bukkit.getPluginManager());
        Bukkit.getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
    }

    private void registerListener(PluginManager pluginManager) {
        HandlerList.unregisterAll(this);

        final CypherInventoryListener inventoryListener = new CypherInventoryListener();
        final CypherPlayerListener playerListener = new CypherPlayerListener();

        pluginManager.registerEvents(inventoryListener, this);
        pluginManager.registerEvents(playerListener, this);
    }
}
