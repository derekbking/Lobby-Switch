package com.cypher.config;

/**
 * Created by Derek on 8/11/2014.
 * Time: 8:34 PM
 */
public abstract class ConfigPaths {

    public static final String SERVER_SLOTS = "Server_Slots";
    public static final String SERVER_SLOT = "Server_Slots.Slot";
    public static final String SERVER_SLOT_MATERIAL = "Server_Slots.Slot.Material";
    public static final String SERVER_SLOT_METADATA = "Server_Slots.Slot.MetaData";
    public static final String SERVER_SLOT_AMOUNT = "Server_Slots.Slot.Amount";
    public static final String SERVER_SLOT_DISPLAY_NAME = "Server_Slots.Slot.Display_Name";
    public static final String SERVER_SLOT_TARGET_SERVER = "Server_Slots.Slot.Target_Server";

    public static final String INVENTORY_ROWS = "Inventory.Rows";
    public static final String INVENTORY_NAME = "Inventory.Name";

    public static final String SELECTOR_MATERIAL = "Selector.Material";
    public static final String SELECTOR_DISPLAY_NAME = "Selector.Display_Name";

    public static final String VERSION = "Version";

    public static final String OLD_INVENTORY_ROWS = "InventoryRows";
    public static final String OLD_INVENTORY_NAME = "InventoryName";

    public static final String OLD_ITEMSTACK = "ItemStack";

    public static final String OLD_SERVERS = "Servers";

    public static String getSlotPath(String path, int slot) {
        String[] split = path.split("\\.");

        if (split.length > 1) {
            split[1] = String.valueOf(slot);
        }
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : split) {
            stringBuilder.append(string);
            stringBuilder.append(".");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }
}
