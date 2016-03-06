package com.lobbyswitch.versions;

import com.lobbyswitch.ServerItem;
import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Derek on 8/16/2014.
 * Time: 12:01 AM
 */
public abstract class Config0_3_5 {

    public static FileConfiguration update(FileConfiguration fileConfiguration) {
        int rows = fileConfiguration.getInt(ConfigPaths.OLD_INVENTORY_ROWS);
        String inventory_name = fileConfiguration.getString(ConfigPaths.OLD_INVENTORY_NAME);
        Material material = fileConfiguration.getItemStack(ConfigPaths.OLD_ITEMSTACK).getType();
        String selector_name = fileConfiguration.getItemStack(ConfigPaths.OLD_ITEMSTACK).getItemMeta().getDisplayName();
        HashMap<Integer, ServerItem> serverItems = new HashMap<>();
        ArrayList<String> serverList = new ArrayList<String>(fileConfiguration.getStringList(ConfigPaths.OLD_SERVERS));
        for (String string : serverList) {
            String[] split = string.split(":");

            serverItems.put(Integer.parseInt(split[5]), new ServerItem(Material.valueOf(split[0]), (byte) 0, String.valueOf(Integer.parseInt(split[1])), split[2], split[3], new ArrayList<String>(), false));
        }
        String version = fileConfiguration.getString(ConfigPaths.VERSION);
        fileConfiguration.set(ConfigPaths.INVENTORY_ROWS, rows);
        fileConfiguration.set(ConfigPaths.INVENTORY_NAME, inventory_name);
        fileConfiguration.set(ConfigPaths.SELECTOR_MATERIAL, material.name());
        fileConfiguration.set(ConfigPaths.SELECTOR_DISPLAY_NAME, "&4" + ChatColor.stripColor(selector_name));
        for (Integer slot : serverItems.keySet()) {
            ServerItem serverItem = serverItems.get(slot);
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_AMOUNT, slot), serverItem.getAmount());
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_DISPLAY_NAME, slot), "&a" + serverItem.getDisplayName());
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_MATERIAL, slot), serverItem.getMaterial().name());
            fileConfiguration.set(ConfigPaths.getSlotPath(ConfigPaths.SERVER_SLOT_TARGET_SERVER, slot), serverItem.getTargetServer());
        }
        fileConfiguration.set(ConfigPaths.VERSION, version);

        fileConfiguration.set(ConfigPaths.OLD_INVENTORY_ROWS, null);
        fileConfiguration.set(ConfigPaths.OLD_INVENTORY_NAME, null);
        fileConfiguration.set(ConfigPaths.OLD_ITEMSTACK, null);
        fileConfiguration.set(ConfigPaths.OLD_SERVERS, null);
        fileConfiguration.set(ConfigPaths.VERSION, "0.3.5");
        return fileConfiguration;
    }

    public static boolean equals(FileConfiguration fileConfiguration) {
        if (fileConfiguration.contains(ConfigPaths.VERSION)) {
            if (fileConfiguration.getString(ConfigPaths.VERSION).equals("0.3.5") ||
                    fileConfiguration.getString(ConfigPaths.VERSION).equals("0.3.6") ||
                    fileConfiguration.get(ConfigPaths.VERSION).equals("0.3.7")) {
                return true;

            }
        }
        return false;
    }
}
