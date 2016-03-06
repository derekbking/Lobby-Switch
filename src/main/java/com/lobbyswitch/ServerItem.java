package com.lobbyswitch;

import com.lobbyswitch.util.ItemUtil;
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
    private String amount;
    private String displayName;
    private String targetServer;
    private List<String> lore;
    private boolean enchanted;

    public ServerItem(Material material, byte metaData, String amount, String displayName, String targetServer, List<String> lore, boolean enchanted) {
        this.material = material;
        this.metaData = metaData;
        this.amount = amount;
        this.displayName = displayName;
        this.targetServer = targetServer;
        this.lore = lore;
        this.enchanted = enchanted;
    }

    public ItemStack getItemStack() {
        int intAmount;

        try {
            intAmount = Integer.valueOf(amount);
        } catch(NumberFormatException e) {
            intAmount = LobbySwitch.p.getServers().get(targetServer).getPlayerCount();
        }

        ItemStack itemStack = new ItemStack(material, intAmount, metaData);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(displayName.replace("&", "\247"));
        List<String> loreFormatted = new ArrayList<>();
        for (String string : lore) {
            loreFormatted.add(string.replace("&", "\247"));
        }
        itemMeta.setLore(loreFormatted);
        itemStack.setItemMeta(itemMeta);

        if (enchanted) {
            itemStack = ItemUtil.addGlow(itemStack);
        }

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

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
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

    public boolean isEnchanted() {
        return enchanted;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
    }
}
