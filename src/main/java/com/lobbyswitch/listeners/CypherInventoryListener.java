package com.lobbyswitch.listeners;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerItem;
import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Derek on 8/5/2014.
 * Time: 3:46 PM
 */
public class CypherInventoryListener implements Listener {

    @EventHandler
    public void onInventoryClickEvent(InventoryClickEvent event) {
        if (event.getClickedInventory() == event.getWhoClicked().getOpenInventory().getTopInventory()) {
            if (event.getClickedInventory().getName().replace("\247", "&").equals(LobbySwitch.p.getConfig().getString(ConfigPaths.INVENTORY_NAME))) {
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

        if (LobbySwitch.p.getConfigManager().getFileConfiguration().getBoolean(ConfigPaths.SELECTOR_SLOT_FORCED)) {
            InventoryView inventoryView = event.getView();
            ItemStack selector = LobbySwitch.p.getConfigManager().getSelector();
            ItemStack affectedStack = inventoryView.getItem(event.getRawSlot());

            if (affectedStack != null && affectedStack.equals(selector)) {
                event.setCancelled(true);
            } else if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
                affectedStack = inventoryView.getItem(event.getHotbarButton());

                if (affectedStack != null && affectedStack.equals(selector)) {
                    event.setCancelled(true);
                } else {
                    affectedStack = inventoryView.getBottomInventory().getItem(event.getHotbarButton());

                    if (affectedStack != null && affectedStack.equals(selector)) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
