package me.mypvp.partyplugin.party;

import me.mypvp.partyplugin.PartyPlugin;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Party {

    private ProxiedPlayer leader;
    private final List<ProxiedPlayer> players;
    private final List<Invitation> invitations;

    public Party(ProxiedPlayer leader) {
        this.leader = leader;

        players = new CopyOnWriteArrayList<>();
        invitations = new CopyOnWriteArrayList<>();

        players.add(leader);
    }

    public void sendMessage(String message) {
        for(ProxiedPlayer player : players) {
            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + message));
        }
    }

    public void invitePlayer(ProxiedPlayer player, ProxiedPlayer invitedPlayer) {
        invitations.add(new Invitation(player, invitedPlayer));

        TextComponent accept = new TextComponent("§a[ANNEHMEN]");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party accept " + player.getName()));

        TextComponent deny = new TextComponent("§c[ABLEHNEN]");
        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party deny " + player.getName()));

        invitedPlayer.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§7Du wurdest von " + player.getName() + " in eine Party eingeladen!\n" +
                PartyPlugin.PREFIX + "§7Klicke hier zum: "), accept, new TextComponent(" "), deny);
        player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§aDu hast §7" + invitedPlayer.getName() + " §azu deiner Party eingeladen!"));
    }

    public void removeInvitation(Invitation invitation) {
        invitations.remove(invitation);
    }

    public Invitation getInvitation(ProxiedPlayer invitedPlayer) {
        for(Invitation invitation : invitations) {
            if(!invitation.getInvitedPlayer().equals(invitedPlayer)) {
                continue;
            }

            return invitation;
        }

        return null;
    }

    public void addPlayer(ProxiedPlayer player) {
        sendMessage(player.getName() + " §7ist der Party beigetreten!");

        players.add(player);
    }

    public void removePlayer(ProxiedPlayer player) {
        if(players.size() < 2) {
            PartyPlugin.getInstance().getPartyManager().deleteParty(this);

            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDie Party wurde aufgelöst!"));

            return;
        }

        players.remove(player);
        player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu hast die Party verlassen!"));

        sendMessage(player.getName() + " §chat die Party verlassen!");

        if(leader.equals(player)) {
            int random = ThreadLocalRandom.current().nextInt(0, players.size());
            ProxiedPlayer newLeader = players.get(random);

            setLeader(newLeader);
        }
    }

    public List<ProxiedPlayer> getPlayers() {
        return players;
    }

    public void setLeader(ProxiedPlayer leader) {
        this.leader = leader;

        sendMessage("§dDie Party hat einen neuen Leiter: §7" + leader.getName());
    }

    public ProxiedPlayer getLeader() {
        return leader;
    }
}
