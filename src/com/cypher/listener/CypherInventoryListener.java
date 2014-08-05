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

/**
 * Created by Derek on 8/5/2014.
 */
public class CypherInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        ItemStack itemStack = event.getInventory().getItem(event.getSlot());
        if(itemStack != null) {
            if(itemStack.getType() == Material.PAPER) {
                ItemMeta itemMeta = itemStack.getItemMeta();
                if(event.getWhoClicked() instanceof Player) {
                    Player player = (Player)event.getWhoClicked();
                    String server = "";
                    if (itemMeta.getDisplayName().equals("Lobby 1")) {
                        server = "Lobby 1";
                    } else if (itemMeta.getDisplayName().equals("Lobby 2")) {
                        server = "Lobby 2";
                    }
                    if(!server.equals("")) {
                        ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();

                        byteArrayDataOutput.writeUTF("Connect");
                        byteArrayDataOutput.writeUTF(server);
                        player.sendPluginMessage(LobbySwitch.p, "BungeeCord", byteArrayDataOutput.toByteArray());
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
