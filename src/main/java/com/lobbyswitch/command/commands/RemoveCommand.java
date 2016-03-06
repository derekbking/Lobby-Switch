package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.command.ICommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

import static com.lobbyswitch.util.ChatUtil.t;

/**
 * Created by derek on 3/6/2016.
 */
public class RemoveCommand implements ICommand {

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"r"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.remove";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label + " <Slot>";
    }

    @Override
    public String getDescription() {
        return "Removes the item in a specified slot for the selector GUI.";
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
        if (args.length != 1) {
            return false;
        }

        try {
            if (!LobbySwitch.p.getConfigManager().removeSlot(Integer.parseInt(args[0]))) {
                sender.sendMessage(t("serverNotFound", args[0]));

                return false;
            }

            sender.sendMessage(t("removedSlot", args[0]));
            return true;
        } catch (NumberFormatException e) {
            sender.sendMessage(t("invalidInteger", args[0]));

            return false;
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        List<String> matches = new ArrayList<>();
        String search = args[args.length - 1].toLowerCase();

        if (args.length == 1) {
            for (String slot : LobbySwitch.p.getConfigManager().getSlots()) {
                if (slot.toLowerCase().startsWith(search)) {
                    matches.add(slot);
                }
            }
        }

        return matches;
    }
}
