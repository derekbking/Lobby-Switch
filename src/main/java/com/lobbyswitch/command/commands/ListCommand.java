package com.lobbyswitch.command.commands;

import com.lobbyswitch.LobbySwitch;
import com.lobbyswitch.ServerItem;
import com.lobbyswitch.command.ICommand;
import org.bukkit.command.CommandSender;

import java.util.List;

import static com.lobbyswitch.util.ChatUtils.t;

/**
 * Created by derek on 3/6/2016.
 */
public class ListCommand implements ICommand {

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String[] getAliases() {
        return new String[] {"l"};
    }

    @Override
    public String getPermission() {
        return "lobbyswitch.list";
    }

    @Override
    public String getUsageString(String label, CommandSender sender) {
        return label;
    }

    @Override
    public String getDescription() {
        return "Displays information about the items in the selector GUI.";
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

        sender.sendMessage(t("serverList"));
        for (String string : LobbySwitch.p.getConfigManager().getSlots()) {
            ServerItem serverItem = LobbySwitch.p.getConfigManager().getServerItem(Integer.parseInt(string));

            sender.sendMessage(t("serverListSlot", string));

            sender.sendMessage(t("serverListSlotInfo", "Amount", serverItem.getAmount()));
            sender.sendMessage(t("serverListSlotInfo", "Display Name", serverItem.getDisplayName()));
            sender.sendMessage(t("serverListSlotInfo", "Material", serverItem.getMaterial().name()));
            sender.sendMessage(t("serverListSlotInfo", "Target Server", serverItem.getTargetServer()));
            sender.sendMessage(t("serverListSlotInfo", "Enchanted", serverItem.isEnchanted() + ""));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String label, String... args) {
        return null;
    }
}
