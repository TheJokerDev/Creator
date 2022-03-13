package me.thejokerdev.creator.events.bungee;

import me.thejokerdev.creator.Bungee;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;

public class ChannelListener implements Listener {
    private final Bungee plugin;

    public ChannelListener(Bungee plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onPluginMessage(PluginMessageEvent e) {
        if (e.getTag().equalsIgnoreCase("BungeeCord")) {
            DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));
            try {
                String channel = in.readUTF(); // channel we delivered
                if(channel.equals("get")){
                    ServerInfo server = plugin.getProxy().getPlayer(e.getReceiver().toString()).getServer().getInfo();
                    String input = in.readUTF(); // the inputstring
                    if(input.equals("nickname")){
                        sendToBukkit(channel, "TheJokerDev", server);
                    } else {
                        sendToBukkit(channel, "0", server);
                    }

                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }

        }
    }

    public void sendToBukkit(String channel, String message, ServerInfo server) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(channel);
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        server.sendData("Creators:messages", stream.toByteArray());

    }
}