package me.thejokerdev.creator.events.spigot;

import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.menus.id.CreatorsMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class LoginEvents implements Listener {
    private Main plugin;

    public LoginEvents(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        new CreatorsMenu(p);
    }
}
