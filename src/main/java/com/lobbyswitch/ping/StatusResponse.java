package com.lobbyswitch.ping;

/**
 * Created by derek on 6/9/2016.
 */
public abstract class StatusResponse {
    private Players players;
    private Version version;
    private String favicon;
    private int time;

    public abstract String getDescription();

    public Players getPlayers() {
        return players;
    }

    public Version getVersion() {
        return version;
    }

    public String getFavicon() {
        return favicon;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

}
