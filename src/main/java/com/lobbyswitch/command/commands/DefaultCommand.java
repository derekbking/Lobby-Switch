package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.command.ICommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.Map;

import static com.lobbyswitch.util.Chatutil.t;

/**
 * Created by derek on 3/6/2016.
 */
public class DefaultCommand implements ICommand {

    @Override
    public String getName() {
        return "";
    }

    @Override
    public String[] getAliases() {
        return new String[0];
    }

    @Override
    public String getPermission() {
        return "lobbyswitch";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label;
    }

    @Override
    public String getDescription() {
        return "Shows a list of the available commands.";
    }

    @Override
    public boolean canBeConsole() {
        return true;
    }

    @Override
    public boolean canBeCommandBlock() {
        return false;
    }

    @Override
    public boolean onCommand(CommandSender sender, String label, String... args) {
        sender.sendMessage(ChatColor.DARK_GREEN + "» " + ChatColor.GREEN + "LobbySwitch Commands");
        for (Map.Entry<String, ICommand> entry : LobbySwitch.p.getCommandManager().getCommandDispatcher().getCommands(sender).entrySet()) {
            sender.sendMessage(t("helpCommand", "lobbyswitch " + entry.getKey(), entry.getValue().getDescription()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        return null;
    }
}
