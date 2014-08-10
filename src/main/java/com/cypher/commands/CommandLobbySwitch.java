package com.cypher.commands;

import com.cypher.LobbySwitch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class CommandLobbySwitch implements TabExecutor {

    private final List<String> arguments = new ArrayList<String>() {
        {
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
                        }
                        return true;
                    case "version":
                    case "v":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Lobby Switch version " + ChatColor.GRAY + LobbySwitch.p.getDescription().getVersion() + ChatColor.RED + ".");
                        return true;
                    default:
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <list:l>");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
                }
            default:
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <list:l>");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String alias, String[] args) {
        if (args.length > 1 || args.length == 0) {
            return new ArrayList<String>();
        }

        List<String> matches = new ArrayList<String>();
        if (args.length == 1) {
            String search = args[0].toLowerCase();
            for (String argument : arguments) {
                if (argument.toLowerCase().startsWith(search)) {
                    matches.add(argument);
                }
            }
        }
        return matches;
    }
}
