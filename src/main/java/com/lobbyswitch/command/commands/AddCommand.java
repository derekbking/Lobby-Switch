package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerItem;
import com.lobbyswitch.command.ICommand;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

import static com.lobbyswitch.util.Chatutil.t;

/**
 * Created by derek on 3/6/2016.
 */
public class AddCommand implements ICommand {

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"a"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.add";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label + " <Item Name:Item ID:Meta Data> <Amount> <Enchanted> <Slot> <Target Server> <Display Name>";
    }

    @Override
    public String getDescription() {
        return "Add a new item to the selector GUI that will connect you to a specified server.";
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
        if (!(args.length > 5)) {
            return false;
        }

        ItemStack itemStack;
        byte metaData;
        boolean enchanted;
        int slot;
        String targetServer;

        try {
            int amount = Integer.parseInt(args[1]);
            if (amount > 64) {
                sender.sendMessage(t("amountMaxExceeded", "64"));
                return false;
            }
            if (amount < 1) {
                sender.sendMessage(t("amountMinExceeded", "0"));
                return false;
            }

            String[] itemStackSplit = args[0].split(":");
            if (itemStackSplit.length > 1) {
                metaData = Byte.valueOf(itemStackSplit[1]);
            } else {
                metaData = (byte) 0;
            }

            try {
                itemStack = new ItemStack(Integer.parseInt(itemStackSplit[0]), amount, metaData);
            } catch (NumberFormatException e) {
                try {
                    itemStack = new ItemStack(Material.valueOf(itemStackSplit[0]), amount, metaData);
                } catch (IllegalArgumentException exception) {
                    sender.sendMessage(t("invalidItem", itemStackSplit[1]));

                    return false;
                }
            }
            if (itemStack.getType() == Material.AIR) {
                sender.sendMessage(t("invalidItem", "AIR"));

                return false;
            }

            if (!args[2].equalsIgnoreCase("true")) {
                if (!args[2].equalsIgnoreCase("false")) {
                    sender.sendMessage(t("invalidBoolean", args[2]));

                    return false;
                }
            }

            enchanted = Boolean.parseBoolean(args[2]);

            try {
                slot = Integer.parseInt(args[3]);
                if (slot < 1) {
                    sender.sendMessage(t("slotMinExceeded", "0"));

                    return false;
                }
                if (slot > LobbySwitch.p.getConfigManager().getInventory().getSize()) {
                    sender.sendMessage(t("slotMaxExceeded", LobbySwitch.p.getConfigManager().getInventory().getSize() + ""));

                    return false;
                }
                if (LobbySwitch.p.getConfigManager().getSlots().contains(String.valueOf(slot))) {
                    sender.sendMessage(t("slotInUse", slot + ""));

                    return false;
                }
            } catch (NumberFormatException e) {
                sender.sendMessage(t("invalidInteger", args[3]));
                return true;
            }
            if (LobbySwitch.p.getServers().keySet().contains(args[4])) {
                targetServer = args[4];
            } else {
                sender.sendMessage(t("invalidServer", args[4]));

                return false;
            }

            StringBuilder stringBuilder = new StringBuilder();

            for (int i = 5; args.length > i; i++) {
                if (i != 5) {
                    stringBuilder.append(" ");
                }
                stringBuilder.append(args[i]);
            }
            ServerItem serverItem = new ServerItem(itemStack.getType(), itemStack.getData().getData(), String.valueOf(itemStack.getAmount()), stringBuilder.toString(), targetServer, new ArrayList<String>(), enchanted);
            LobbySwitch.p.getConfigManager().saveServerItem(serverItem, slot);
            sender.sendMessage(t("itemCreated", slot + "", serverItem.getAmount(), serverItem.getDisplayName(), serverItem.getMaterial().toString(), serverItem.getTargetServer(), serverItem.isEnchanted() + ""));

        } catch (NumberFormatException e) {
            sender.sendMessage(t("invalidInteger", args[1]));
            return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            for (Material material : Material.values()) {
                if (material.name().toLowerCase().startsWith(search)) {
                    matches.add(material.name());
                }
            }
        }

        if (args.length == 2) {
            for (int i = 1; 64 >= i; i++) {
                if (String.valueOf(i).toLowerCase().startsWith(search)) {
                    matches.add(String.valueOf(i));
                }
            }
        }

        if (args.length == 3) {
            if ("true".startsWith(search)) {
                matches.add("true");
            }
            if ("false".startsWith(search)) {
                matches.add("false");
            }
        }

        if (args.length == 4) {
            ArrayList<String> used = new ArrayList<String>(LobbySwitch.p.getConfigManager().getSlots());
            for (int i = 1; LobbySwitch.p.getConfigManager().getInventory().getSize() >= i; i++) {
                if (String.valueOf(i).toLowerCase().startsWith(search) && !used.contains(String.valueOf(i))) {
                    matches.add(String.valueOf(i));
                }
            }
        }

        if (args.length == 5) {
            for (String string : LobbySwitch.p.getServers().keySet()) {
                if (string.toLowerCase().startsWith(search)) {
                    matches.add(string);
                }
            }
        }

        return matches;
    }
}