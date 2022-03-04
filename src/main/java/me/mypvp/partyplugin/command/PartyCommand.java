package me.mypvp.partyplugin.command;

import me.mypvp.partyplugin.PartyPlugin;
import me.mypvp.partyplugin.party.Invitation;
import me.mypvp.partyplugin.party.Party;
import me.mypvp.partyplugin.party.PartyManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class PartyCommand extends Command {

    private final PartyManager partyManager;

    public PartyCommand(PartyManager partyManager) {
        super("party", null, "p");

        this.partyManager = partyManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu musst ein Spieler sein!"));

            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) sender;

        if(args.length > 0) {
            Party party = partyManager.getParty(player);

            if(args.length > 1) {
                ProxiedPlayer secondPlayer = ProxyServer.getInstance().getPlayer(args[1]);

                if(secondPlayer == null) {
                    player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDieser Spieler existiert nicht!"));

                    return;
                }

                if(secondPlayer.equals(player)) {
                    player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu kannst nicht mit dir selbst interagieren!"));

                    return;
                }

                Party secondParty;
                Invitation invitation;

                switch (args[0].toUpperCase()) {
                    case "INVITE":
                        if(party == null) {
                            party = partyManager.createParty(player);
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§dEine neue Party wurde erstellt!"));
                        }

                        if(partyManager.getParty(secondPlayer) != null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDieser Spieler ist bereits in einer anderen Party!"));

                            return;
                        }

                        if(!party.getLeader().equals(player)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist nicht der Leiter!"));

                            return;
                        }

                        if(party.getInvitation(secondPlayer) != null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDieser Spieler wurde bereits in deine Party eingeladen!"));

                            return;
                        }

                        party.invitePlayer(player, secondPlayer);

                        return;
                    case "ACCEPT":
                        if(party != null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist bereits in einer anderen Party!"));

                            return;
                        }

                        secondParty = partyManager.getParty(secondPlayer);

                        if(secondParty == null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDer Spieler der dich eingeladen hat, ist in keiner Party!"));

                            return;
                        }

                        invitation = secondParty.getInvitation(player);

                        if(invitation == null || !invitation.getPlayer().equals(secondPlayer)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDiese Einladung ist ungültig!"));

                            return;
                        }

                        if((System.currentTimeMillis() - invitation.getSessionTime()) / 1000 > 60) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDiese Einladung ist bereits abgelaufen!"));
                        } else {
                            secondParty.addPlayer(player);
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§aDu hast die Einladung angenommen!"));
                        }

                        secondParty.removeInvitation(invitation);

                        return;
                    case "DENY":
                        secondParty = partyManager.getParty(secondPlayer);

                        if(secondParty == null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDer Spieler der dich eingeladen hat, ist in keiner Party!"));

                            return;
                        }

                        invitation = secondParty.getInvitation(player);

                        if(invitation == null || !invitation.getPlayer().equals(secondPlayer)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDiese Einladung ist ungültig!"));

                            return;
                        }

                        if((System.currentTimeMillis() - invitation.getSessionTime()) / 1000 > 60) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDiese Einladung ist bereits abgelaufen!"));
                        } else {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu hast die Einladung abgelehnt!"));
                        }

                        secondParty.removeInvitation(invitation);

                        return;
                    case "KICK":
                        if(party == null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist in keiner Party!"));

                            return;
                        }

                        if(!party.getLeader().equals(player)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist nicht der Leiter!"));

                            return;
                        }

                        if(!party.getPlayers().contains(secondPlayer)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDieser Spieler befindet sich nicht in deiner Party!"));

                            return;
                        }

                        party.removePlayer(secondPlayer);

                        return;
                    case "PROMOTE":
                        if(party == null) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist in keiner Party!"));

                            return;
                        }

                        if(!party.getLeader().equals(player)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist nicht der Leiter!"));

                            return;
                        }

                        if(!party.getPlayers().contains(secondPlayer)) {
                            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDieser Spieler befindet sich nicht in deiner Party!"));

                            return;
                        }

                        party.setLeader(secondPlayer);
                        return;
                }
            }

            switch (args[0].toUpperCase()) {
                case "LIST":
                    if(party == null) {
                        player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist in keiner Party!"));

                        return;
                    }

                    StringBuilder builder = new StringBuilder();
                    builder.append(PartyPlugin.PREFIX + "§dFolgende Mitglieder sind in deiner Party:\n");

                    for(int i = 0; i < party.getPlayers().size(); i++) {
                        builder.append(PartyPlugin.PREFIX + "§d- §7" + party.getPlayers().get(i).getName());

                        if(i == party.getPlayers().size() - 1) {
                            break;
                        }

                        builder.append("\n");
                    }

                    player.sendMessage(new TextComponent(builder.toString()));

                    return;
                case "LEAVE":
                    if(party == null) {
                        player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist in keiner Party!"));

                        return;
                    }

                    party.removePlayer(player);
                    return;
            }
        }

        player.sendMessage(new TextComponent(
                PartyPlugin.PREFIX + "§d/party help §8× §7Listet alle Commands auf\n" +
                PartyPlugin.PREFIX + "§d/party invite <Player> §8× §7Lädt einen Spieler in deine Party ein\n" +
                PartyPlugin.PREFIX + "§d/party accept <Player> §8× §7Akzeptiert eine Party-Einladung\n" +
                PartyPlugin.PREFIX + "§d/party deny <Player> §8× §7Lehnt eine Party-Einladung ab\n" +
                PartyPlugin.PREFIX + "§d/party kick <Player> §8× §7Schmeißt einen Spieler aus der Party\n" +
                PartyPlugin.PREFIX + "§d/party promote <Player> §8× §7Befördert einen Spieler\n" +
                PartyPlugin.PREFIX + "§d/party list §8× §7Listet alle Spieler in der Party auf\n" +
                PartyPlugin.PREFIX + "§d/party leave §8× §7Verlässt die aktuelle Party\n" +
                PartyPlugin.PREFIX + "§d#p <Message> §8× §7Sendet eine Nachricht in den Party-Chat"));
    }

}
