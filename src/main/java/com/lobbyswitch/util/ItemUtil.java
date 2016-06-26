package com.lobbyswitch.util;

import com.lobbyswitch.util.versions.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * Created by derek on 12/9/2015.
 */
public class ItemUtil {

    private static IGlow iGlow;

    static {
        String version = Bukkit.getServer().getClass().getPackage().getName().replace(".",  ",").split(",")[3];

        if (version.equals("v1_8_R1")) {
            iGlow = new Glow1_8_R1();
        } else if (version.equals("v1_8_R2")) {
            iGlow = new Glow1_8_R2();
        } else if (version.equals("v1_8_R3")) {
            iGlow = new Glow1_8_R3();
        } else if (version.equals("v1_9_R1")) {
            iGlow = new Glow1_9_R1();
        } else if (version.equals("v1_9_R2")) {
            iGlow = new Glow1_9_R2();
        } else {
            iGlow = new Glow1_10_R1();
        }
    }

    public static ItemStack addGlow(ItemStack item){
        return iGlow.addGlow(item);
    }
}
