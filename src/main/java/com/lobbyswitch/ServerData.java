package com.lobbyswitch;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lobbyswitch.config.ConfigPaths;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Derek on 12/5/2014.
 * Time: 12:31 PM
 */
public class ServerData implements PluginMessageListener {

    String name;
    String ip;
    short port;
    String MOTD = "Offline";
    int playerCount = 0;

    public ServerData(String name) {
        this.name = name;
        new BukkitRunnable() {
            @Override
            public void run() {
                updateData();
                updateInventories();
            }
        }.runTaskTimerAsynchronously(LobbySwitch.p, 20, LobbySwitch.p.getConfig().getInt(ConfigPaths.MOTD_REFRESH_RATE));
    }

    public void updateData() {
        if (!LobbySwitch.p.getServer().getOnlinePlayers().isEmpty()) {
            Player player = (Player) LobbySwitch.p.getServer().getOnlinePlayers().toArray()[0];
            if (LobbySwitch.p.getServers().keySet().contains(name)) {
                ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                byteArrayDataOutput.writeUTF("ServerIP");
                byteArrayDataOutput.writeUTF(name);
                player.sendPluginMessage(LobbySwitch.p, LobbySwitch.p.getPluginChannel(), byteArrayDataOutput.toByteArray());

                byteArrayDataOutput = ByteStreams.newDataOutput();
                byteArrayDataOutput.writeUTF("PlayerCount");
                byteArrayDataOutput.writeUTF(name);
                player.sendPluginMessage(LobbySwitch.p, LobbySwitch.p.getPluginChannel(), byteArrayDataOutput.toByteArray());
            }
        }
    }

    public void updateInventories() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getOpenInventory() != null) {
                if (player.getOpenInventory().getTopInventory() != null) {
                    if (player.getOpenInventory().getTopInventory().getName().equals(LobbySwitch.p.getConfig().getString(ConfigPaths.INVENTORY_NAME))) {
                        player.getOpenInventory().getTopInventory().setContents(LobbySwitch.p.getConfigManager().getInventory().getContents());
                    }
                }
            }
        }
    }

    public String getIp() {
        return ip;
    }

    public String getMOTD() {
        return MOTD;
    }

    public short getPort() {
        return port;
    }

    public String getName() {
        return name;
    }
    public int getPlayerCount() {
        return playerCount;
    }

    private String getNewMOTD() {
        String returnString;
        ServerListPing serverListPing = new ServerListPing();

        serverListPing.setAddress(new InetSocketAddress(ip, port));
        try {
            ServerListPing.StatusResponse statusResponse = serverListPing.fetchData();
            returnString = statusResponse.getDescription();
        } catch (IOException e) {
            returnString = "Offline";
        }
        return returnString;
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals(LobbySwitch.p.getPluginChannel())) {
            return;
        }

        try {
            ByteArrayDataInput byteArrayDataInput = ByteStreams.newDataInput(message);
            String subChannel = byteArrayDataInput.readUTF();

            if (subChannel.equals("ServerIP")) {
                if (byteArrayDataInput.readUTF().equals(name)) {
                    ip = byteArrayDataInput.readUTF();
                    port = byteArrayDataInput.readShort();
                    MOTD = getNewMOTD().replace("Â", "");
                }
            }

            if (subChannel.equals("PlayerCount")) {
                String server = byteArrayDataInput.readUTF();
                int playerCount = byteArrayDataInput.readInt();

                if (server.equals(name)) {
                    this.playerCount = playerCount;
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
