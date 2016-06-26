package com.lobbyswitch.util.versions;

import net.minecraft.server.v1_8_R2.NBTTagCompound;
import net.minecraft.server.v1_8_R2.NBTTagList;
import org.bukkit.craftbukkit.v1_8_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

/**
 * Created by derek on 6/23/2016.
 */
public class Glow1_8_R2 implements IGlow {

    @Override
    public ItemStack addGlow(ItemStack itemStack) {
        net.minecraft.server.v1_8_R2.ItemStack nmsStack = CraftItemStack.asNMSCopy(itemStack);
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
