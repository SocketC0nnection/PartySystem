package me.mypvp.partyplugin.listener;

import me.mypvp.partyplugin.PartyPlugin;
import me.mypvp.partyplugin.party.Party;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ChatListener implements Listener {

    @EventHandler
    public void onChat(ChatEvent event) {
        if(!(event.getSender() instanceof ProxiedPlayer)) {
            return;
        }

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        String[] args = event.getMessage().split(" ");

        if(args.length < 2 || !args[0].equalsIgnoreCase("#p")) {
            return;
        }

        Party party = PartyPlugin.getInstance().getPartyManager().getParty(player);

        if(party == null) {
            player.sendMessage(new TextComponent(PartyPlugin.PREFIX + "§cDu bist in keiner Party!"));

            event.setCancelled(true);
            return;
        }

        StringBuilder builder = new StringBuilder();

        for(int i = 1; i < args.length; i++) {
            builder.append(args[i]);

            if(i == args.length - 1) {
                break;
            }

            builder.append(" ");
        }

        event.setCancelled(true);
        party.sendMessage(player.getName() + " §d-> §7" + builder.toString());
    }

}
