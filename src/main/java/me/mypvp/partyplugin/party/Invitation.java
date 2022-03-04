package me.mypvp.partyplugin.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Invitation {

    private final ProxiedPlayer player;
    private final ProxiedPlayer invitedPlayer;

    private final long sessionTime;

    public Invitation(ProxiedPlayer player, ProxiedPlayer invitedPlayer) {
        this.player = player;
        this.invitedPlayer = invitedPlayer;

        sessionTime = System.currentTimeMillis();
    }

    public long getSessionTime() {
        return sessionTime;
    }

    public ProxiedPlayer getPlayer() {
        return player;
    }

    public ProxiedPlayer getInvitedPlayer() {
        return invitedPlayer;
    }
}
