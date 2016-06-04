package com.lobbyswitch.config;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerData;
import com.lobbyswitch.ServerItem;
import com.lobbyswitch.util.ChatUtil;
import com.lobbyswitch.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

/**
 * Created by Derek on 8/11/2014.
 * Time: 8:26 PM
 */
public class ConfigManager {

    private FileConfiguration fileConfiguration;

    public ConfigManager(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public ItemStack getSelector() {
        ItemStack itemStack = new ItemStack(Material.valueOf((String) fileConfiguration.get(ConfigPaths.SELECTOR_MATERIAL)), 1);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(fileConfiguration.getString(ConfigPaths.SELECTOR_DISPLAY_NAME).replace("&", "\247"));
        itemStack.setItemMeta(itemMeta);
        if (fileConfiguration.getBoolean(ConfigPaths.SELECTOR_ENCHANTED)) {
            itemStack = ItemUtil.addGlow(itemStack);
        }
        return itemStack;
    }

    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(null, 9 * fileConfiguration.getInt(ConfigPaths.INVENTORY_ROWS), fileConfiguration.getString(ConfigPaths.INVENTORY_NAME).replace("&", "\247"));

        for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
            ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));
            if (serverItem != null) {
                ItemStack serverItemStack = serverItem.getItemStack();
                if (LobbySwitch.p.getServers().keySet().contains(serverItem.getTargetServer())) {
                    ServerData serverData = LobbySwitch.p.getServers().get(serverItem.getTargetServer());
                    if (serverItemStack.getItemMeta() != null) {
                        ItemMeta itemMeta = serverItemStack.getItemMeta();

                        String displayName = itemMeta.getDisplayName();

                        displayName = ChatUtil.replaceLowerCasePlaceholder(displayName, "%PLAYER_COUNT%", serverData.getPlayerCount());
                        displayName = ChatUtil.replaceLowerCasePlaceholder(displayName, "%TARGET_MOTD%", serverData.getMOTD());

                        itemMeta.setDisplayName(displayName);

                        List<String> loreLines = new ArrayList<>();
                        if (itemMeta.getLore() != null) {
                            for (String loreLine : itemMeta.getLore()) {
                                loreLine = ChatUtil.replaceLowerCasePlaceholder(loreLine, "%PLAYER_COUNT%", serverData.getPlayerCount());
                                loreLine = ChatUtil.replaceLowerCasePlaceholder(loreLine, "%TARGET_MOTD%", serverData.getMOTD());
                                if (loreLine.contains("\n")) {
                                    loreLines.addAll(Arrays.asList(loreLine.split("\n")));
                                } else {
                                    loreLines.add(loreLine);
                                }
                            }
                        }
                        itemMeta.setLore(loreLines);
                        serverItemStack.setItemMeta(itemMeta);
                    }
                }
                inventory.setItem(Integer.parseInt(string) - 1, serverItemStack);
            }
        }

        return inventory;
    }

    public Set<String> getSlots() {
        return fileConfiguration.getConfigurationSection(ConfigPaths.SERVER_SLOTS).getKeys(false);
    }

    public ServerItem getServerItem(int slot) {
        Material material;
        byte metaData;
        String amount;
        String displayName;
        String targetServer;
        List<String> lore;
        boolean enchanted;
        if (getSlots().contains(String.valueOf(slot))) {
            amount = fileConfiguration.getString(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_AMOUNT, slot));
            displayName = fileConfiguration.getString(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_DISPLAY_NAME, slot));
            material = Material.valueOf(fileConfiguration.getString(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_MATERIAL, slot)));
            if (fileConfiguration.contains(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_METADATA, slot))) {
                metaData = Byte.valueOf(fileConfiguration.getString(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_METADATA, slot)));
            } else {
                metaData = (byte) 0;
            }
            targetServer = fileConfiguration.getString(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_TARGET_SERVER, slot));
            lore = fileConfiguration.getStringList(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_LORE, slot));
            enchanted = fileConfiguration.getBoolean(ConfigPaths.getSlotPath(ConfigPaths.SERVER_ENCHANTED, slot));
        } else {
            return null;
        }
        return new ServerItem(material, metaData, amount, displayName, targetServer, lore, enchanted);
    }

    public void saveServerItem(ServerItem serverItem, int slot) {
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_AMOUNT, slot), serverItem.getAmount());
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_DISPLAY_NAME, slot), serverItem.getDisplayName());
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_MATERIAL, slot), serverItem.getMaterial().name());
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_METADATA, slot), String.valueOf(serverItem.getMetaData()));
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_TARGET_SERVER, slot), serverItem.getTargetServer());
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_LORE, slot), serverItem.getLore());
        fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_ENCHANTED, slot), serverItem.isEnchanted());
        LobbySwitch.p.saveConfig();
    }

    public boolean removeSlot(int slot) {
        if (getSlots().contains(String.valueOf(slot))) {
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT, slot), null);
            LobbySwitch.p.saveConfig();
            return true;
        } else {
            return false;
        }
    }
}
