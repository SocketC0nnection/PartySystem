package me.mypvp.partyplugin.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PartyManager {

    private final List<Party> parties;

    public PartyManager() {
        parties = new CopyOnWriteArrayList<>();
    }

    public Party createParty(ProxiedPlayer leader) {
        Party party = new Party(leader);
        parties.add(party);

        return party;
    }

    public void deleteParty(Party party) {
        parties.remove(party);
    }

    public Party getParty(ProxiedPlayer player) {
        for(Party party : parties) {
            if(!party.getPlayers().contains(player)) {
                continue;
            }

            return party;
        }

        return null;
    }

}
