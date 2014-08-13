package com.cypher.config;

import com.cypher.LobbySwitch;
import com.cypher.ServerItem;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Derek on 8/9/2014.
 * Time: 1:25 PM
 */
public class ConfigUpdater {

    private FileConfiguration fileConfiguration;
    private String fromVersion;

    public ConfigUpdater(FileConfiguration fileConfiguration) {
        this.fileConfiguration = fileConfiguration;
    }

    public void update() {
        ArrayList<String> keySet = new ArrayList<>(fileConfiguration.getConfigurationSection("").getKeys(false));
        if (keySet.isEmpty()) {
            fileConfiguration.set(ConfigPaths.VERSION, LobbySwitch.p.getDescription().getVersion());
            LobbySwitch.p.saveConfig();
            return;
        }
        if (keySet.contains("Servers")) {
            ArrayList<String> servers = (ArrayList) fileConfiguration.getList("Servers");

            String string = servers.get(0);
            String[] split = string.split(":");

            if (split.length == 5) {
                fromVersion = "0.2.1";
            } else if (split.length == 6) {
                fromVersion = "0.2.2";
            } else {
                fromVersion = "Unknown";
            }
        } else {
            fromVersion = "0.3";
        }
        if (keySet.contains(ConfigPaths.SERVER_SLOTS)) {
            fromVersion = "0.3.1";
        }

        if (fromVersion.equals("0.2.1")) {
            ArrayList<String> newServerList = new ArrayList<String>();
            for (int i = 0; i < fileConfiguration.getList("Servers").size(); i++) {
                String string = fileConfiguration.getStringList("Servers").get(i);
                String split = string + ":" + (i + 1);
                newServerList.add(split);
            }
            fileConfiguration.set("Servers", newServerList);
            fromVersion = "0.2.2";
        }
        if (fromVersion.equals("0.2.2") || fromVersion.equals("0.3")) {
            int rows = fileConfiguration.getInt("InventoryRows");
            String inventory_name = fileConfiguration.getString("InventoryName");
            Material material = fileConfiguration.getItemStack("ItemStack").getType();
            String selector_name = fileConfiguration.getItemStack("ItemStack").getItemMeta().getDisplayName();
            HashMap<Integer, ServerItem> serverItems = new HashMap<Integer, ServerItem>();
            ArrayList<String> serverList = new ArrayList<String>(fileConfiguration.getStringList("Servers"));
            for (String string : serverList) {
                String[] split = string.split(":");

                serverItems.put(Integer.parseInt(split[5]), new ServerItem(Material.valueOf(split[0]), Integer.parseInt(split[1]), split[2], split[3]));
            }
            String version = fileConfiguration.getString("Version");
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

            fileConfiguration.set("InventoryRows", null);
            fileConfiguration.set("InventoryName", null);
            fileConfiguration.set("ItemStack", null);
            fileConfiguration.set("Servers", null);
        }
        fileConfiguration.set(ConfigPaths.VERSION, LobbySwitch.p.getDescription().getVersion());
        LobbySwitch.p.saveConfig();
    }
}
