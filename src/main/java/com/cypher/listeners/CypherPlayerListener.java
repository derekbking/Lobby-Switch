package com.cypher.listeners;

import com.cypher.LobbySwitch;
import com.cypher.ServerItem;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;

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
        ItemStack itemStack = event.getPlayer().getItemInHand();
        if (itemStack.getType() == LobbySwitch.p.getConfigManager().getSelector().getType()) {
            if (itemStack.getItemMeta().getDisplayName().equals(LobbySwitch.p.getConfigManager().getSelector().getItemMeta().getDisplayName())) {
                Inventory inventory = LobbySwitch.p.getConfigManager().getInventory();

                for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
                    ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));
                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();

                    byteArrayDataOutput.writeUTF("PlayerCount");
                    byteArrayDataOutput.writeUTF(serverItem.getTargetServer());
                    event.getPlayer().sendPluginMessage(LobbySwitch.p, "BungeeCord", byteArrayDataOutput.toByteArray());

                    inventory.setItem(Integer.parseInt(string) - 1, serverItem.getItemStack());
                }
                event.getPlayer().openInventory(inventory);
            }
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();

        if (subChannel.equals("PlayerCount")) {
            String server = in.readUTF();
            int playerCount = in.readInt();
            Inventory inventory = player.getOpenInventory().getTopInventory();
            for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
                ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));

                if (serverItem.getTargetServer().equals(server)) {
                    ItemStack itemStack = inventory.getItem(Integer.parseInt(string) - 1);
                    ItemMeta itemMeta = itemStack.getItemMeta();
                    itemMeta.setLore(Arrays.asList(String.valueOf(playerCount) + " Online"));
                    itemStack.setItemMeta(itemMeta);
                }
            }
        }

        if (subChannel.equals("GetServers")) {
            LobbySwitch.p.setServers(new ArrayList(Arrays.asList(in.readUTF().split(", "))));
        }
    }
}
