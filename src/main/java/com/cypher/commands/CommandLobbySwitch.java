package com.cypher.commands;

import com.cypher.LobbySwitch;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommandLobbySwitch implements TabExecutor {

    private final List<String> arguments = new ArrayList<String>() {
        {
            add("add");
            add("list");
            add("version");
        }
    };

    private String PREFIX = "\u00bb ";

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender == LobbySwitch.p.getServer().getConsoleSender()) {
            PREFIX = "> ";
        }
        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "list":
                    case "l":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Server list");
                        for (String string : (ArrayList<String>) LobbySwitch.p.getFileConfig().getList("Servers")) {
                            String[] split = string.split(":");
                            commandSender.sendMessage("  " + ChatColor.DARK_RED + PREFIX + ChatColor.GRAY + split[2]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "ItemStack: " + ChatColor.GRAY + split[0]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Amount: " + ChatColor.GRAY + split[1]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Color: " + ChatColor.GRAY + ChatColor.getByChar(split[4]).name());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Slot: " + ChatColor.GRAY + split[5]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Target: " + ChatColor.GRAY + split[3]);
                        }
                        return true;
                    case "version":
                    case "v":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Lobby Switch version " + ChatColor.GRAY + LobbySwitch.p.getDescription().getVersion() + ChatColor.RED + ".");
                        return true;
                    default:
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <list:l>");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
                        return true;
                }
            default:
                if (args.length > 6) {
                    if (args[0].equals("add") || args[0].equals("a")) {
                        ItemStack itemStack;
                        int slot;
                        String targetServer;
                        String color;
                        String displayName;

                        try {
                            int amount = Integer.parseInt(args[2]);
                            if (amount > 64) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount can't exceed " + ChatColor.GRAY + "64" + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            if (amount < 1) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The amount must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            try {
                                itemStack = new ItemStack(Integer.parseInt(args[1]), amount);
                            } catch (NumberFormatException e) {
                                try {
                                    itemStack = new ItemStack(Material.valueOf(args[1]), amount);
                                } catch (IllegalArgumentException exception) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[1] + ChatColor.RED + "\"" + " is not a valid item name or id.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                            }
                            try {
                                slot = Integer.parseInt(args[3]);
                                if (slot < 1) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be greater than " + ChatColor.GRAY + "0" + ChatColor.RED + ".");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                                if (slot > LobbySwitch.p.getFileConfig().getInt("InventoryRows") * 9) {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The slot number must be less than or equal to " + ChatColor.GRAY + LobbySwitch.p.getFileConfig().getInt("InventoryRows") * 9 + ChatColor.RED + ".");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                            } catch (NumberFormatException e) {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[3] + ChatColor.RED + "\"" + " is not a valid integer.");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            if (LobbySwitch.p.getServers().contains(args[4])) {
                                targetServer = args[4];
                            } else {
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[4] + ChatColor.RED + "\"" + " is not a valid target server.");
                                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                return true;
                            }
                            try {
                                color = String.valueOf(ChatColor.valueOf(args[5]).getChar());
                            } catch (IllegalArgumentException e) {
                                if (args[5].length() == 1) {
                                    try {
                                        color = String.valueOf(ChatColor.getByChar(args[5]).getChar());
                                    } catch (NullPointerException exception) {
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[5] + ChatColor.RED + "\"" + " is not a valid chat color.");
                                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                        return true;
                                    }
                                } else {
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[5] + ChatColor.RED + "\"" + " is not a valid chat color.");
                                    commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                                    return true;
                                }
                            }
                            StringBuilder stringBuilder = new StringBuilder();

                            for (int i = 6; args.length > i; i++) {
                                if (i != 6) {
                                    stringBuilder.append(" ");
                                }
                                stringBuilder.append(args[i]);
                            }
                            displayName = stringBuilder.toString();
                            String serverString = itemStack.getType().name() + ":" + itemStack.getAmount() + ":" + displayName + ":" + targetServer + ":" + color + ":" + slot;
                            ArrayList<String> arrayList = (ArrayList) LobbySwitch.p.getFileConfig().getList("Servers");
                            arrayList.add(serverString);
                            LobbySwitch.p.getFileConfig().set("Servers", arrayList);
                            LobbySwitch.p.saveConfig();
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Added new server");
                            String[] split = serverString.split(":");
                            commandSender.sendMessage("  " + ChatColor.DARK_RED + PREFIX + ChatColor.GRAY + split[2]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "ItemStack: " + ChatColor.GRAY + split[0]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Amount: " + ChatColor.GRAY + split[1]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Color: " + ChatColor.GRAY + ChatColor.getByChar(split[4]).name());
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Slot: " + ChatColor.GRAY + split[5]);
                            commandSender.sendMessage("    " + ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Target: " + ChatColor.GRAY + split[3]);

                        } catch (NumberFormatException e) {
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "The value \"" + ChatColor.GRAY + args[2] + ChatColor.RED + "\"" + " is not a valid integer.");
                            commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                            return true;
                        }
                        return true;
                    }
                }
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <add|a> <ItemName|ItemID> <Amount> <Slot> <Target Server> <Color> <Display Name>");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <list:l>");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();
        if (args.length == 1) {
            for (String argument : arguments) {
                if (argument.toLowerCase().startsWith(search)) {
                    matches.add(argument);
                }
            }
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (Material argument : Material.values()) {
                    if (argument.name().toLowerCase().startsWith(search)) {
                        matches.add(argument.name());
                    }
                }
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (int i = 1; 64 >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search)) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (int i = 1; LobbySwitch.p.getFileConfig().getInt("InventoryRows") * 9 >= i; i++) {
                    if (String.valueOf(i).toLowerCase().startsWith(search)) {
                        matches.add(String.valueOf(i));
                    }
                }
            }
        }
        if (args.length == 5) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (String string : LobbySwitch.p.getServers()) {
                    if (string.toLowerCase().startsWith(search)) {
                        matches.add(string);
                    }
                }
            }
        }
        if (args.length == 6) {
            if (args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("a")) {
                for (ChatColor chatColor : ChatColor.values()) {
                    if (chatColor.name().toLowerCase().startsWith(search)) {
                        matches.add(chatColor.name());
                    }
                }
            }
        }

        return matches;
    }
}
