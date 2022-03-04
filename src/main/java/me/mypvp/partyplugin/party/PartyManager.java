package me.mypvp.partyplugin.party;

import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class PartyManager {

    private final ArrayList<Party> parties;

    public PartyManager() {
        parties = new ArrayList<>();
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
