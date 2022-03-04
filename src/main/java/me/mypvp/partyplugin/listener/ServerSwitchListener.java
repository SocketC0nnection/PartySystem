package me.mypvp.partyplugin.listener;

import me.mypvp.partyplugin.PartyPlugin;
import me.mypvp.partyplugin.party.Party;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitchListener implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer player = event.getPlayer();
        Party party = PartyPlugin.getInstance().getPartyManager().getParty(player);
        ServerInfo server = player.getServer().getInfo();

        if(party == null || !party.getLeader().equals(player) || server.getName().toLowerCase().contains("lobby")) {
            return;
        }

        party.sendMessage("§dDie Party betritt den §5" + server.getName() + " §dServer!");

        for(ProxiedPlayer players : party.getPlayers()) {
            if(players.getServer().getInfo().equals(server)) {
                continue;
            }

            players.connect(player.getServer().getInfo());
        }
    }

}
