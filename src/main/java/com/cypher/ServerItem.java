package com.cypher;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by Derek on 8/11/2014.
 * Time: 8:19 PM
 */
public class ServerItem {

    private Material material;
    private int amount;
    private String displayName;
    private String targetServer;

    public ServerItem(Material material, int amount, String displayName, String targetServer) {
        this.material = material;
        this.amount = amount;
        this.displayName = displayName;
        this.targetServer = targetServer;
    }

    public ItemStack getItemStack() {
        ItemStack itemStack = new ItemStack(material, amount);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(displayName);
        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
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
}
