package me.mypvp.partyplugin.listener;

import me.mypvp.partyplugin.PartyPlugin;
import me.mypvp.partyplugin.party.Party;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PlayerDisconnectListener implements Listener {

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Party party = PartyPlugin.getInstance().getPartyManager().getParty(player);

        if(party == null) {
            return;
        }

        party.removePlayer(player);
    }

}
