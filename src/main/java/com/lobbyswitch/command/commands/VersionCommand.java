package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.command.ICommand;
import org.bukkit.command.CommandSender;

import java.util.List;

import static com.lobbyswitch.util.ChatUtil.t;

/**
 * Created by derek on 3/6/2016.
 */
public class VersionCommand implements ICommand {

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"v"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.version";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label;
    }

    @Override
    public String getDescription() {
        return "Shows the version of Lobby Switch that you are using.";
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
        if (args.length != 0) {
            return false;
        }

        sender.sendMessage(t("version", LobbySwitch.p.getDescription().getVersion()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        return null;
    }
}
