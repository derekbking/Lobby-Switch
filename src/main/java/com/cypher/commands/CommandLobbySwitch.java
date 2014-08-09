package com.cypher.commands;

import com.cypher.LobbySwitch;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.List;

public class CommandLobbySwitch implements TabExecutor {

    private String PREFIX = "\u00bb ";

    @Override
    public boolean onCommand(final CommandSender commandSender, Command command, String label, String[] args) {
        if (commandSender == LobbySwitch.p.getServer().getConsoleSender()) {
            PREFIX = "> ";
        }
        switch (args.length) {
            case 1:
                switch (args[0].toLowerCase()) {
                    case "version":
                    case "v":
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "Lobby Switch version " + ChatColor.GRAY + LobbySwitch.p.getDescription().getVersion() + ChatColor.RED + ".");
                        return true;
                    default:
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                        commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
                }
            default:
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + ChatColor.BOLD + "Invalid command format");
                commandSender.sendMessage(ChatColor.DARK_RED + PREFIX + ChatColor.RED + "/lobbyswitch <version:v>");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
        return null;
    }
}
