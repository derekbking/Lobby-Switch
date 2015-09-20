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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Derek on 12/5/2014.
 * Time: 12:31 PM
 */
public class ServerData implements PluginMessageListener {

    private ExecutorService executorService = null;

    private String name;
    private String ip;
    private short port;
    private String MOTD = "Offline";
    private int playerCount = 0;

    public ServerData(String name) {
        this.name = name;
        new BukkitRunnable() {
            @Override
            public void run() {
                updateData();
                updateInventories();
            }
        }.runTaskTimerAsynchronously(LobbySwitch.p, 20, LobbySwitch.p.getConfig().getInt(ConfigPaths.MOTD_REFRESH_RATE));

        this.executorService = Executors.newFixedThreadPool(1);
    }

    public void updateData() {
        if (!LobbySwitch.p.getServer().getOnlinePlayers().isEmpty()) {
            final Player player = (Player) LobbySwitch.p.getServer().getOnlinePlayers().toArray()[0];
            if (LobbySwitch.p.getServers().keySet().contains(name)) {
                if (LobbySwitch.p.getServer().getName().equals(name)) {
                    ip = LobbySwitch.p.getServer().getIp();
                    port = (short) LobbySwitch.p.getServer().getPort();
                    playerCount = LobbySwitch.p.getServer().getOnlinePlayers().size();
                } else {
                    ByteArrayDataOutput byteArrayDataOutput = ByteStreams.newDataOutput();
                    byteArrayDataOutput.writeUTF("ServerIP");
                    byteArrayDataOutput.writeUTF(name);
                    player.sendPluginMessage(LobbySwitch.p, LobbySwitch.p.getPluginChannel(), byteArrayDataOutput.toByteArray());

                    getOnlinePlayers(new Callback<Integer>() {
                        @Override
                        public void onSuccess(Integer value) {
                            playerCount = value;
                        }
                    });
                }
                getNewMOTD(new Callback<String>() {
                    @Override
                    public void onSuccess(String value) {
                        MOTD = value.replace("Â", "");
                    }
                });
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

    private void getNewMOTD(final Callback<String> callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                String returnString;
                ServerListPing serverListPing = new ServerListPing();

                serverListPing.setAddress(new InetSocketAddress(ip, port));
                try {
                    ServerListPing.StatusResponse statusResponse = serverListPing.fetchData();
                    returnString = statusResponse.getDescription();
                } catch (IOException e) {
                    returnString = "Offline";
                }
                callback.onSuccess(returnString);
            }
        });
    }

    private void getOnlinePlayers(final Callback<Integer> callback) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                int onlinePlayers;
                ServerListPing serverListPing = new ServerListPing();

                serverListPing.setAddress(new InetSocketAddress(ip, port));
                try {
                    ServerListPing.StatusResponse statusResponse = serverListPing.fetchData();
                    onlinePlayers = statusResponse.getPlayers().getOnline();
                } catch (IOException e) {
                    onlinePlayers = 0;
                }
                callback.onSuccess(onlinePlayers);
            }
        });
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
                    getNewMOTD(new Callback<String>() {
                        @Override
                        public void onSuccess(String value) {
                            MOTD = value.replace("Â", "");
                        }
                    });
                }
            }
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }
}
