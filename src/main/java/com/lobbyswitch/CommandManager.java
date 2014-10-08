package com.lobbyswitch;

import com.lobbyswitch.commands.CommandLobbySwitch;
import org.bukkit.command.PluginCommand;

/**
 * Created by Derek on 8/8/2014.
 * Time: 5:54 PM
 */
public class CommandManager {

    private static void registerLobbySwitchCommand() {
        PluginCommand command = LobbySwitch.p.getCommand("lobbyswitch");
        command.setExecutor(new CommandLobbySwitch());
    }

    public static void registerCommands() {
        registerLobbySwitchCommand();
    }
}
