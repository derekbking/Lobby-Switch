package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.command.ICommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static com.lobbyswitch.util.ChatUtils.t;

/**
 * Created by derek on 3/6/2016.
 */
public class OpenCommand implements ICommand {

    @Override
    public String getName() {
        return "open";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"o"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.open";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label + " <Player>";
    }

    @Override
    public String getDescription() {
        return "Opens the selector GUI for a specified player";
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
        if (args.length != 1) {
            return false;
        }

        Player player = (Bukkit.getPlayer(args[0]));

        if (player == null || !player.isOnline()) {
            sender.sendMessage(t("playerNotFound", args[0]));

            return false;
        }

        player.openInventory(LobbySwitch.p.getConfigManager().getInventory());
        sender.sendMessage(t("inventoryOpened", player.getName()));

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.getName().toLowerCase().startsWith(search)) {
                    matches.add(player.getName());
                }
            }
        }

        return matches;
    }
}
