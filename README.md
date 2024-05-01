# LobbySwitch Plugin

## Overview

LobbySwitch is a versatile Minecraft plugin that allows you to teleport across servers using a configurable inventory GUI. It is designed to be configured in-game for ease and safety. This plugin is open-source, so feel free to contribute on GitHub.

## Features

-   **Teleport Across Servers:** Use a configurable inventory GUI.
-   **In-Game Configuration Commands:** Easily adjust settings within the game.
-   **Placeholders Support:** Dynamic data in item stacks and more.

## Commands

-   `/lobbyswitch|ls`: Display a list of commands.
-   `/lobbyswitch <add|a> <ItemName|ItemID:MetaData> <Amount> <Slot> <Target Server> <Display Name>`: Add items.
-   `/lobbyswitch <edit|e> <Slot> <Amount|Lore|Material|Name|Slot|Target> <New Amount|New Material:MetaData|New Name|New Slot|New Target>`: Edit items (Use `/n` for line breaks in lore).
-   `/lobbyswitch <list|l>`: Display all active servers and info about the display item.
-   `/lobbyswitch <open|o> <PLAYER>`: Open the selector inventory for another player.
-   `/lobbyswitch <remove|r> <Slot>`: Remove a server from the inventory.
-   `/lobbyswitch <version|v>`: Display the version of Lobby Switch you are running.

## Placeholders

-   `%PLAYER_COUNT%`: Replaced with the number of players on that server. Can be used in `Amount`, `Lore`, `Item Name`.
-   `%TARGET_MOTD%`: Replaced with the server's MOTD. Can be used in `Lore`, `Item Name`.

## Configuration

-   **Inventory Settings:**
    -   `Rows`: 1 (Max of 6)
    -   `Name`: Server Selector (Use `&6` for color)
-   **Lore Refresh Rate**: 300 ticks
-   **Selector:**
    -   `Add_On_Join`: true
    -   `Slot`:
        -   `Forced`: false
        -   `Position`: -1
    -   `Material`: COMPASS
    -   `Display_Name`: '&4Server Selector'

## Permissions

-   **All Commands**: `lobbyswitch`

## Contributors

-   **Lazertx** - Plugin Developer
-   **ClubCraft Network** - Plugin Suggestions

## Reporting Issues

Please report any issues in the discussion section. Include all relevant information such as plugin version, server version, config files, error logs, and, if possible, screenshots or videos to reproduce the bug. Your detailed reports help us continue improving this plugin.

## Note

Development may be sporadic due to limited time. Future updates will occur as time and inspiration allow.
