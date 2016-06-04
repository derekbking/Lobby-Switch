package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerItem;
import com.lobbyswitch.command.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.lobbyswitch.util.Chatutil.t;

/**
 * Created by derek on 3/6/2016.
 */
public class EditCommand implements ICommand {

    private final List<String> editSubArguments = new ArrayList<String>() {
        {
            add("amount");
            add("enchanted");
            add("lore");
            add("material");
            add("name");
            add("slot");
            add("target");
        }
    };

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"e"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.edit";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label + " <Slot> <Amount:Lore:Material:Name:Slot:Target Server> <New Amount:New Item Name:New Item ID:New Meta Data:New Name:New Slot:New Target>";
    }

    @Override
    public String getDescription() {
        return "Modify items in the selector GUI.";
    }

    @Override
    public boolean canBeConsole() {
        return true;
    }

    @Override
    public boolean canBeCommandBlock() {
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String... args) {
        if (args.length < 3) {
            return false;
        }

        int slot;
        try {
            slot = Integer.parseInt(args[0]);
            if (slot < 1) {
                sender.sendMessage(t("slotMinExceeded", 0 + ""));

                return false;
            }
            if (slot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                sender.sendMessage(t("slotMaxExceeded", LobbySwitch.p.getConfigManager().getInventory().getSize() + ""));

                return false;
            }
            if (!LobbySwitch.p.getConfigManager().getSlots().contains(String.valueOf(slot))) {
                sender.sendMessage(t("noItem", slot + ""));

                return false;
            }
        } catch (NumberFormatException e) {
            sender.sendMessage(t("invalidInteger", args[0]));

            return false;
        }

        ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(slot);
        switch (args[1].toLowerCase()) {
            case "amount":
                String amount;
                try {
                    amount = String.valueOf(Integer.parseInt(args[2]));

                    if (Integer.valueOf(amount) > 64) {
                        sender.sendMessage(t("amountMaxExceeded", "64"));

                        return false;
                    }
                    if (Integer.valueOf(amount) < 1) {
                        sender.sendMessage(t("amountMinExceeded", "0"));

                        return false;
                    }
                } catch (NumberFormatException e) {
                    if (args[2].equalsIgnoreCase("%PLAYER_COUNT%")) {
                        amount = "%PLAYER_COUNT%";
                    } else {
                        sender.sendMessage(t("invalidInteger", args[2]));

                        return false;
                    }
                }

                serverItem.setAmount(amount);
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);

                sender.sendMessage(t("setAmount", amount));

                return true;
            case "enchanted":
                boolean enchanted;

                if (!args[2].equalsIgnoreCase("true")) {
                    if (!args[2].equalsIgnoreCase("false")) {
                        sender.sendMessage(t("invalidBoolean", args[2]));
                        return false;
                    }
                }

                enchanted = Boolean.parseBoolean(args[2]);
                serverItem.setEnchanted(enchanted);
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                sender.sendMessage(t("setEnchanted", enchanted + ""));
                return true;
            case "material":
                Material material;
                byte metaData;
                String[] itemStackSplit = args[2].split(":");
                if (itemStackSplit.length > 1) {
                    metaData = Byte.valueOf(itemStackSplit[1]);
                } else {
                    metaData = (byte) 0;
                }
                try {
                    material = Material.getMaterial(Integer.parseInt(itemStackSplit[0]));
                    if (material == null) {
                        sender.sendMessage(t("invalidItem", args[2]));

                        return false;
                    }
                } catch (NumberFormatException e) {
                    try {
                        material = Material.valueOf(itemStackSplit[0]);
                    } catch (IllegalArgumentException exception) {
                        sender.sendMessage(t("invalidItem", args[2]));

                        return false;
                    }
                }
                if (material == Material.AIR) {
                    sender.sendMessage(t("invalidItem", "AIR"));

                    return false;
                }
                serverItem.setMaterial(material);
                serverItem.setMetaData(metaData);
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                sender.sendMessage(t("setMaterial", material.name()));

                return true;
            case "slot":
                int newSlot;
                try {
                    newSlot = Integer.parseInt(args[2]);
                    if (newSlot < 1) {
                        sender.sendMessage(t("slotMinExceeded", "0"));

                        return false;
                    }
                    if (newSlot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                        sender.sendMessage(t("slotMaxExceeded", LobbySwitch.p.getConfigManager().getInventory().getSize() + ""));

                        return false;
                    }
                    if (slot == newSlot) {
                        sender.sendMessage(t("sameSlot"));

                        return false;
                    }
                    if (LobbySwitch.p.getConfigManager().getSlots().contains(String.valueOf(newSlot))) {
                        sender.sendMessage(t("slotInUse", newSlot + ""));

                        return false;
                    }

                    LobbySwitch.p.getConfigManager().removeSlot(slot);
                    LobbySwitch.p.getConfigManager().saveServerItem(serverItem, newSlot);
                    sender.sendMessage(t("setSlot", slot + "", newSlot + ""));
                } catch (NumberFormatException e) {
                    sender.sendMessage(t("invalidInteger", args[2]));

                    return false;
                }
                return true;
            case "target":
                String targetServer;
                if (LobbySwitch.p.getServers().keySet().contains(args[3])) {
                    targetServer = args[2];
                } else {
                    sender.sendMessage(t("invalidServer", args[2]));

                    return false;
                }
                serverItem.setTargetServer(targetServer);
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                sender.sendMessage(t("setTarget", targetServer));
                return true;
            case "name":
                StringBuilder stringBuilder = new StringBuilder();

                for (int i = 2; args.length > i; i++) {
                    if (i != 2) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(args[i]);
                }
                serverItem.setDisplayName(stringBuilder.toString());
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                sender.sendMessage(t("setDisplayName", stringBuilder.toString()));
                return true;
            case "lore":
                stringBuilder = new StringBuilder();

                for (int i = 2; args.length > i; i++) {
                    if (i != 2) {
                        stringBuilder.append(" ");
                    }
                    stringBuilder.append(args[i]);
                }

                serverItem.setLore(Arrays.asList(stringBuilder.toString().split("/n")));
                LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
                sender.sendMessage(t("setLore"));
                for (String string : serverItem.getLore()) {
                    sender.sendMessage("    " + ChatColor.WHITE + string.replace("&", "\247"));
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            for (String slot : LobbySwitch.p.getConfigManager().getSlots()) {
                if (slot.toLowerCase().startsWith(search)) {
                    matches.add(slot);
                }
            }
        }

        if (args.length == 2) {
            for (String argument : editSubArguments) {
                if (argument.toLowerCase().startsWith(search)) {
                    matches.add(argument);
                }
            }
        }

        if (args.length == 3) {
            if (args[1].equalsIgnoreCase("amount")) {
                for (int i = 1; 64 >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search)) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
            if (args[1].equalsIgnoreCase("enchanted")) {
                if ("true".startsWith(search)) {
                    matches.add("true");
                }
                if ("false".startsWith(search)) {
                    matches.add("false");
                }
            }
            if (args[1].equalsIgnoreCase("material")) {
                for (Material argument : Material.values()) {
                    if (argument.name().toLowerCase().startsWith(search)) {
                        matches.add(argument.name());
                    }
                }
            }
            if (args[1].equalsIgnoreCase("slot")) {
                ArrayList<String> used = new ArrayList<>(LobbySwitch.p.getConfigManager().getSlots());
                for (int i = 1; LobbySwitch.p.getConfigManager().getInventory().getSize() >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search) && !used.contains(String.valueOf(i))) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
            if (args[1].equalsIgnoreCase("target")) {
                for (String string : LobbySwitch.p.getServers().keySet()) {
                    if (string.toLowerCase().startsWith(search)) {
                        matches.add(string);
                    }
                }
            }
        }

        return matches;
    }
}
