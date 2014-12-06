package com.lobbyswitch.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerData;
import com.lobbyswitch.ServerItem;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:49 PM
 */
public class CypherPlayerListener implements Listener, PluginMessageListener {

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

        if (!event.getPlayer().getInventory().contains(LobbySwitch.p.getConfigManager().getSelector())) {
            event.getPlayer().getInventory().addItem(LobbySwitch.p.getConfigManager().getSelector());
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
            ItemStack itemStack = event.getPlayer().getItemInHand();
            if (itemStack.getType() == LobbySwitch.p.getConfigManager().getSelector().getType()) {
                if (itemStack.getItemMeta().getDisplayName().equals(LobbySwitch.p.getConfigManager().getSelector().getItemMeta().getDisplayName())) {
                    Inventory inventory = LobbySwitch.p.getConfigManager().getInventory();

                    for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
                        ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));
                        if (serverItem != null) {
                            ItemStack serverItemStack = serverItem.getItemStack();
                            if (LobbySwitch.p.getServers().keySet().contains(serverItem.getTargetServer())) {
                                ServerData serverData = LobbySwitch.p.getServers().get(serverItem.getTargetServer());
                                if (serverItemStack.getItemMeta() != null) {
                                    ItemMeta itemMeta = serverItemStack.getItemMeta();
                                    List<String> loreLines = new ArrayList<>();
                                    if (itemMeta.getLore() != null) {
                                        for (String loreLine : itemMeta.getLore()) {
                                            loreLine = loreLine.replace("%PLAYER_COUNT%", String.valueOf(serverData.getPlayerCount()));
                                            loreLine = loreLine.replace("%TARGET_MOTD%", serverData.getMOTD());
                                            loreLines.add(loreLine);
                                        }
                                    }
                                    itemMeta.setLore(loreLines);
                                    serverItemStack.setItemMeta(itemMeta);
                                }
                            }
                            inventory.setItem(Integer.parseInt(string) - 1, serverItemStack);
                        }
                    }
                    event.getPlayer().openInventory(inventory);
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
