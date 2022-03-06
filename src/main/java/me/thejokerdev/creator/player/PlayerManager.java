package me.thejokerdev.creator.player;

import me.thejokerdev.creator.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

public class PlayerManager implements Listener {
    private Main plugin;
    private final HashMap<String, CPlayer> players;
    private final HashMap<UUID, CPlayer> playersUUID;

    public PlayerManager(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        players = new HashMap<>();
        playersUUID = new HashMap<>();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();
        CPlayer player = new CPlayer(p);
        players.put(p.getName(), player);
        playersUUID.put(p.getUniqueId(), player);

        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getDataManager().getData().getData(player);
            }
        }.runTaskAsynchronously(plugin);
    }

    public CPlayer getPlayer(Player p){
        return players.getOrDefault(p.getName(), playersUUID.getOrDefault(p.getUniqueId(), null));
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e){
        Player p = e.getPlayer();
        CPlayer player = getPlayer(p);
        new BukkitRunnable() {
            @Override
            public void run() {
                plugin.getDataManager().getData().syncData(player);
            }
        }.runTaskAsynchronously(plugin);

        players.remove(p.getName());
        playersUUID.remove(p.getUniqueId());
    }
}
