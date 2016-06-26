package com.lobbyswitch.util.versions;

import net.minecraft.server.v1_10_R1.NBTTagCompound;
import net.minecraft.server.v1_10_R1.NBTTagList;
import org.bukkit.craftbukkit.v1_10_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by derek on 6/25/2016.
 */
public class Glow1_10_R1 implements IGlow {

    @Override
    public ItemStack addGlow(ItemStack itemStack) {
        net.minecraft.server.v1_10_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        return CraftItemStack.asCraftMirror(nmsStack);
    }
}
