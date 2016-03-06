package com.lobbyswitch.command;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * Created by Derek on 2/16/2015.
 * Time: 5:42 PM
 */
public interface ICommand {

    String getName();

    String[] getAliases();

    String getPermission();

    String getUsageString(String label, CommandSender sender);

    String getDescription();

    boolean canBeConsole();

    boolean canBeCommandBlock();

    boolean onCommand(CommandSender sender, String label, String... args);

    List<String> onTabComplete(CommandSender sender, String label, String... args);
}
