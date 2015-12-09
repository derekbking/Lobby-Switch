package com.lobbyswitch.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.config.ConfigManager;
import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:49 PM
 */
public class CypherPlayerListener implements Listener, PluginMessageListener {

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        ConfigManager configManager = LobbySwitch.p.getConfigManager();

        if (!configManager.getFileConfiguration().getBoolean(ConfigPaths.SELECTOR_DROPPABLE)) {
            if (event.getItemDrop().getItemStack().equals(configManager.getSelector())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        LobbySwitch.p.getServer().getScheduler().scheduleSyncDelayedTask(LobbySwitch.p, new Runnable() {
            @Override
            public void run() {
                ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                byteArrayDataOutput.writeUTF("GetServers");
                event.getPlayer().sendPluginMessage(LobbySwitch.p, "BungeeCord", byteArrayDataOutput.toByteArray());
            }
        }, 20);

        FileConfiguration fileConfiguration = LobbySwitch.p.getConfigManager().getFileConfiguration();

        if (fileConfiguration.getBoolean(ConfigPaths.ADD_ON_JOIN)) {
            int slotPosition = fileConfiguration.getInt(ConfigPaths.SELECTOR_SLOT_POSITION);
            ItemStack selector = LobbySwitch.p.getConfigManager().getSelector();

            if (slotPosition == -1) {
                if (!event.getPlayer().getInventory().contains(selector)) {
                    event.getPlayer().getInventory().addItem(selector);
                }
            } else {
                event.getPlayer().getInventory().remove(selector);
                event.getPlayer().getInventory().setItem(slotPosition - 1, selector);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemStack = event.getPlayer().getItemInHand();
            if (itemStack.getType() == LobbySwitch.p.getConfigManager().getSelector().getType()) {
                if (itemStack.getItemMeta().getDisplayName().equals(LobbySwitch.p.getConfigManager().getSelector().getItemMeta().getDisplayName())) {
                    event.getPlayer().openInventory(LobbySwitch.p.getConfigManager().getInventory());
                }
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(LobbySwitch.p.getPluginChannel())) {
            return;
        }

        try {
            ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(message);
            String subChannel = byteArrayDataInput.readUTF();

            if (subChannel.equals("GetServers")) {
                for (String server : byteArrayDataInput.readUTF().split(", ")) {
                    LobbySwitch.p.addServer(server);
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
