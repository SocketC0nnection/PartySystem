package me.mypvp.partyplugin;

import me.mypvp.partyplugin.listener.ChatListener;
import me.mypvp.partyplugin.listener.PlayerDisconnectListener;
import net.md_5.bungee.api.plugin.Plugin;
import me.mypvp.partyplugin.command.PartyCommand;
import me.mypvp.partyplugin.listener.ServerSwitchListener;
import me.mypvp.partyplugin.party.PartyManager;
import net.md_5.bungee.api.plugin.PluginManager;

public class PartyPlugin extends Plugin {

    private static PartyPlugin instance;
    private PartyManager partyManager;

    public static final String PREFIX = "§8» §dParty §8┃ §7";

    @Override
    public void onEnable() {
        instance = this;
        partyManager = new PartyManager();

        PluginManager pluginManager = getProxy().getPluginManager();

        pluginManager.registerListener(this, new ServerSwitchListener());
        pluginManager.registerListener(this, new PlayerDisconnectListener());
        pluginManager.registerListener(this, new ChatListener());

        pluginManager.registerCommand(this, new PartyCommand(partyManager));
    }

    public PartyManager getPartyManager() {
        return partyManager;
    }

    public static PartyPlugin getInstance() {
        return instance;
    }

}
