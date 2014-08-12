package com.cypher.listeners;

import com.cypher.LobbySwitch;
import com.cypher.ServerItem;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:46 PM
 */
public class CypherInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getWhoClicked().getOpenInventory().getTopInventory()) {
            if (event.getInventory().getSize() >= event.getSlot() && event.getSlot() >= 0) {
                ItemStack itemStack = event.getInventory().getItem(event.getSlot());
                if (itemStack != null) {
                    if (event.getWhoClicked() instanceof Player) {
                        for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
                            if (event.getSlot() == Integer.parseInt(string) - 1) {
                                Player player = (Player) event.getWhoClicked();
                                ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));

                                ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();

                                byteArrayDataOutput.writeUTF("Connect");
                                byteArrayDataOutput.writeUTF(serverItem.getTargetServer());
                                player.sendPluginMessage(LobbySwitch.p, "BungeeCord", byteArrayDataOutput.toByteArray());
                                event.setCancelled(true);
                            }
                        }
                    }
                }
            }
        }
    }
}
