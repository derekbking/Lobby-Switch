package com.cypher.listener;

import com.cypher.LobbySwitch;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Created by Derek on 8/5/2014.
 */
public class CypherInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getWhoClicked().getOpenInventory().getTopInventory()) {
            if (event.getInventory().getSize() >= event.getSlot() && event.getSlot() >= 0) {
                ItemStack itemStack = event.getInventory().getItem(event.getSlot());
                if (itemStack != null) {
                    if (event.getWhoClicked() instanceof Player) {
                        for (String string : (ArrayList<String>) LobbySwitch.p.getFileConfig().getList("Servers")) {
                            String[] split = string.split(":");
                            ItemStack currentItemStack = new ItemStack(Material.valueOf(split[0]), Integer.valueOf(split[1]));
                            ItemMeta currentItemMeta = itemStack.getItemMeta();
                            currentItemMeta.setDisplayName("\247" + split[4] + split[2]);
                            currentItemStack.setItemMeta(currentItemMeta);

                            if (itemStack.getType() == currentItemStack.getType()) {
                                ItemMeta itemMeta = itemStack.getItemMeta();
                                Player player = (Player) event.getWhoClicked();

                                if (itemMeta.getDisplayName().equals(currentItemMeta.getDisplayName())) {
                                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();

                                    byteArrayDataOutput.writeUTF("Connect");
                                    byteArrayDataOutput.writeUTF(split[3]);
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
}
