package com.cypher.listener;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Derek on 8/5/2014.
 */
public class CypherPlayerListener implements Listener {

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(event.getPlayer().getItemInHand().getType() == Material.GLOWSTONE_DUST) {
            Inventory inventory = Bukkit.createInventory(event.getPlayer(), 9);

            ItemStack lobby1 = new ItemStack(Material.PAPER, 1);
            ItemStack lobby2 = new ItemStack(Material.PAPER, 1);

            ItemMeta lobby1Meta = lobby1.getItemMeta();
            ItemMeta lobby2Meta = lobby2.getItemMeta();

            lobby1Meta.setDisplayName("Lobby 1");
            lobby2Meta.setDisplayName("Lobby 2");

            lobby1.setItemMeta(lobby1Meta);
            lobby2.setItemMeta(lobby2Meta);

            inventory.addItem(lobby1);
            inventory.addItem(lobby2);

            event.getPlayer().openInventory(inventory);
        }
    }
}
