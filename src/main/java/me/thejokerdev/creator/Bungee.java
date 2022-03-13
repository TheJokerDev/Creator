package me.thejokerdev.creator;

import me.thejokerdev.creator.events.bungee.ChannelListener;
import net.md_5.bungee.api.plugin.Plugin;

public class Bungee extends Plugin {

    @Override
    public void onEnable(){
        getProxy().getPluginManager().registerListener(this, new ChannelListener(this));

        getProxy().registerChannel("Creators:messages");
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }
}
