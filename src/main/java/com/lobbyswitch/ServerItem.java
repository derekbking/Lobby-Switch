package com.lobbyswitch;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Derek on 8/11/2014.
 * Time: 8:19 PM
 */
public class ServerItem {

    private Material material;
    private byte metaData;
    private int amount;
    private String displayName;
    private String targetServer;
    private List<String> lore;

    public ServerItem(Material material, byte metaData, int amount, String displayName, String targetServer, List<String> lore) {
        this.material = material;
        this.metaData = metaData;
        this.amount = amount;
        this.displayName = displayName;
        this.targetServer = targetServer;
        this.lore = lore;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(material, amount, metaData);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName.replace("&", "\247"));
        List<String> loreFormatted = new ArrayList<>();
        for (String string : lore) {
            loreFormatted.add(string.replace("&", "\247"));
        }
        itemMeta.setLore(loreFormatted);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public byte getMetaData() {
        return metaData;
    }

    public void setMetaData(byte metaData) {
        this.metaData = metaData;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getTargetServer() {
        return targetServer;
    }

    public void setTargetServer(String targetServer) {
        this.targetServer = targetServer;
    }

    public List<String> getLore() {
        return lore;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }
}
