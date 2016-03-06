package com.lobbyswitch.command;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.command.commands.*;
import org.bukkit.command.PluginCommand;

/**
 * Created by Derek on 2/16/2015.
 * Time: 5:44 PM
 */
public class CommandManager {

    private final CommandDispatcher commandDispatcher;

    public CommandManager() {
        PluginCommand pluginCommand = LobbySwitch.p.getCommand("lobbyswitch");
        commandDispatcher = new CommandDispatcher("lobbyswitch", "LobbySwitch's main command.");

        commandDispatcher.setDefault(new DefaultCommand());
        commandDispatcher.registerCommand(new AddCommand());
        commandDispatcher.registerCommand(new DefaultCommand());
        commandDispatcher.registerCommand(new EditCommand());
        commandDispatcher.registerCommand(new ListCommand());
        commandDispatcher.registerCommand(new OpenCommand());
        commandDispatcher.registerCommand(new RemoveCommand());
        commandDispatcher.registerCommand(new VersionCommand());

        pluginCommand.setExecutor(commandDispatcher);
        pluginCommand.setTabCompleter(commandDispatcher);
    }

    public CommandDispatcher getCommandDispatcher() {
        return commandDispatcher;
    }
}
