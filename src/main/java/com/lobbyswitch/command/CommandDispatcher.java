package com.lobbyswitch.command;

import com.lobbyswitch.LobbySwitch;
import org.bukkit.ChatColor;
import org.bukkit.command.*;

import java.util.*;

import static com.lobbyswitch.util.Chatutil.t;

/**
 * Created by Derek on 2/16/2015.
 * Time: 5:44 PM
 */
public class CommandDispatcher implements CommandExecutor, TabCompleter {

    private final String commandName;
    private final String commandDescription;
    private final Map<String, ICommand> subCommands;
    private ICommand command;

    public CommandDispatcher(String commandName, String description) {
        this.commandName = commandName;
        this.commandDescription = description;
        subCommands = new LinkedHashMap<>();
    }


    public void registerCommand(ICommand command) {
        subCommands.put(command.getName(), command);
    }

    public void setDefault(ICommand command) {
        this.command = command;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 && this.command == null) {
            displayUsage(sender, label, null);
            return true;
        }

        ICommand com = null;
        String subCommand = "";

        String[] subArgs = args;

        if (args.length > 0) {
            subCommand = args[0].toLowerCase();
            subArgs = (args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);

            if (subCommands.containsKey(subCommand)) {
                com = subCommands.get(subCommand);
            } else {
                AliasCheck:
                for (Map.Entry<String, ICommand> ent : subCommands.entrySet()) {
                    if (ent.getValue().getAliases() != null) {
                        String[] aliases = ent.getValue().getAliases();
                        for (String alias : aliases) {
                            if (subCommand.equalsIgnoreCase(alias)) {
                                com = ent.getValue();
                                break AliasCheck;
                            }
                        }
                    }
                }
            }
        }

        if (com == null) {
            com = this.command;
        }

        if (com == null) {
            displayUsage(sender, label, subCommand);
            return true;
        }

        if (!com.canBeConsole() && (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender)) {
            if (com == this.command) {
                displayUsage(sender, label, subCommand);
            }
            else {
                sender.sendMessage(t("noConsole", label + " " + subCommand));
            }
            return true;
        }
        if (!com.canBeCommandBlock() && sender instanceof BlockCommandSender) {
            if (com == this.command) {
                displayUsage(sender, label, subCommand);
            } else {
                sender.sendMessage(t("noCommandBlock", label + " " + subCommand));
            }
            return true;
        }
        if (com.getPermission() != null && !sender.hasPermission(com.getPermission())) {
            StringBuilder stringBuilder = new StringBuilder(label);
            stringBuilder.append(" ");
            stringBuilder.append(subCommand);
            for (String string : subArgs) {
                stringBuilder.append(" ");
                stringBuilder.append(string);
            }
            sender.sendMessage(t("noPermission", com.getPermission(), stringBuilder.toString()));
            return true;
        }

        if (!com.onCommand(sender, subCommand, subArgs)) {
            sender.sendMessage(t("incorrectFormat", "/" + commandName + " " + com.getUsageString(subCommand, sender)));
        }

        return true;
    }

    private void displayUsage(CommandSender sender, String label, String subCommand) {
        Map<String, ICommand> commands = LobbySwitch.p.getCommandManager().getCommandDispatcher().getCommands(sender);

        if (commands.isEmpty()) {
            sender.sendMessage(t("noCommands"));
        } else {
            sender.sendMessage(ChatColor.DARK_GREEN + "» " + ChatColor.GREEN + "LobbySwitch Commands");

            for (Map.Entry<String, ICommand> entry : commands.entrySet()) {
                sender.sendMessage(ChatColor.GREEN + "/" + label + " " +entry.getKey() + ChatColor.GRAY + " " + entry.getValue().getDescription());
            }
        }
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> results = new ArrayList<>();
        if (args.length == 1) {
            for (ICommand registeredCommand : subCommands.values()) {
                if (registeredCommand.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
                    if (!registeredCommand.canBeConsole() && (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender)) {
                        continue;
                    }
                    if (registeredCommand.getPermission() != null && !sender.hasPermission(registeredCommand.getPermission())) {
                        continue;
                    }

                    results.add(registeredCommand.getName());
                }
            }
        } else {
            String subCommand = args[0].toLowerCase();
            String[] subArgs = (args.length > 1 ? Arrays.copyOfRange(args, 1, args.length) : new String[0]);

            ICommand com = null;
            if (subCommands.containsKey(subCommand)) {
                com = subCommands.get(subCommand);
            } else {
                AliasCheck:
                for (Map.Entry<String, ICommand> ent : subCommands.entrySet()) {
                    if (ent.getValue().getAliases() != null) {
                        String[] aliases = ent.getValue().getAliases();
                        for (String alias : aliases) {
                            if (subCommand.equalsIgnoreCase(alias)) {
                                com = ent.getValue();
                                break AliasCheck;
                            }
                        }
                    }
                }
            }

            if (com == null) {
                return results;
            }
            if (!com.canBeConsole() && (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender)) {
                return results;
            }
            if (com.getPermission() != null && !sender.hasPermission(com.getPermission())) {
                return results;
            }

            results = com.onTabComplete(sender, subCommand, subArgs);
            if (results == null) {
                return new ArrayList<>();
            }
        }
        return results;
    }

    public String getCommandHelp() {
        return commandName + commandDescription;
    }

    public Map<String, ICommand> getCommands(CommandSender commandSender) {
        Map<String, ICommand> commands = new LinkedHashMap<>();

        commands.put("", command);
        commands.putAll(subCommands);
        List<String> toRemove = new ArrayList<>();
        for (String name : commands.keySet()) {
            ICommand iCommand = commands.get(name);
            if (iCommand.getPermission() != null) {
                if (!commandSender.hasPermission(iCommand.getPermission())) {
                    toRemove.add(name);
                }
            }
        }
        for (String string : toRemove) {
            commands.remove(string);
        }
        return commands;
    }
}
