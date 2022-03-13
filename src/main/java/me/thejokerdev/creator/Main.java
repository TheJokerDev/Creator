package me.thejokerdev.creator;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import lombok.Getter;
import lombok.Setter;
import me.thejokerdev.creator.data.DataManager;
import me.thejokerdev.creator.lang.LangManager;
import me.thejokerdev.creator.menus.MenuListener;
import me.thejokerdev.creator.player.PlayerManager;
import me.thejokerdev.creator.utils.Hastebin;
import me.thejokerdev.creator.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
@Setter
public final class Main extends JavaPlugin implements PluginMessageListener {
    private static Main plugin;
    private LangManager langManager;
    private DataManager dataManager;
    private PlayerManager playerManager;
    private CreatorsManager creatorManager;

    private List<String> pluginMSG = new ArrayList<>();

    private Hastebin hastebin;

    @Override
    public void onEnable() {
        long ms = System.currentTimeMillis();
        plugin = this;
        hastebin = new Hastebin();
        saveDefaultConfig();
        console(
                "&f&m ============| &bCreator &av."+getDescription().getVersion()+" &eby &9"+getDescription().getAuthors().get(0)+" &f&m|============ ",
                "",
                " &fLoading managers: "
        );
        if (managers()){
            console("  &a✓ &fManagers enabled correctly.");
        } else {
            console("  &c✗ &fManagers not enabled correctly.");
        }

        checkIfBungee();
        PluginManager pm = getServer().getPluginManager();
        if (!pm.isPluginEnabled(this)) {
            return;
        }

        pm.registerEvents(new MenuListener(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getMessenger().registerIncomingPluginChannel( this, "Creators:messages", this );

        ms = System.currentTimeMillis()-ms;
        console(" &aPlugin correctly loaded in "+ms+" ms.",
                "&f&m ======================================================== ");
    }

    public static Main getPlugin() {
        return plugin;
    }

    private void checkIfBungee()
    {
        if ( !getServer().spigot().getConfig().getConfigurationSection("settings").getBoolean( "settings.bungeecord" ) )
        {
            console( "{prefix}&cThis server is not BungeeCord.",
                    "If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.",
                    "Plugin disabled!"
            );
            getServer().getPluginManager().disablePlugin( this );
        }
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes)
    {
        if ( !channel.equalsIgnoreCase( "Creators:messages" ) )
        {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput( bytes );
        String subChannel = in.readUTF();
        if ( subChannel.equalsIgnoreCase( "Update" ) )
        {
            String data = in.readUTF();
            String[] split = data.split("`");
            String example = "2b733549-d2cc-40f0-b7f3-9bfa9f3c1b89`TheJokerDev`1`Sat Mar 12 22:21:12 CST 2022";
            if (split.length == 4){
                String uuid = split[0];
                if (pluginMSG.contains(uuid)){
                    pluginMSG.remove(uuid);
                    return;
                }

                String creator = split[1];
                int votes = Integer.parseInt(split[2]);
                String date = split[3];

                creatorManager.updateCreator(creator, votes, date);
            }
        }
    }

    boolean managers(){
        try {
            langManager = new LangManager(this);
            langManager.load();
        } catch (Exception e) {
            error(e);
            return false;
        }
        try {
            dataManager = new DataManager(this);
            dataManager.initData();
        } catch (Exception e) {
            error(e);
            return false;
        }
        try {
            playerManager = new PlayerManager(this);
        } catch (Exception e) {
            error(e);
            return false;
        }
        try {
            creatorManager = new CreatorsManager(this);
            creatorManager.init();
        } catch (Exception e) {
            error(e);
            return false;
        }
        return true;
    }

    public void error(Exception e){
        console(
                "  &c⚠ Error:",
                "    &cClass: "+e.getClass().getCanonicalName(),
                "",
                "    &cContact with &bTheJokerDev&c and give to him this link:",
                "    &cFull error: "+hastebin.paste(e.getStackTrace().toString()),
                ""
        );
    }

    void console(String... msg){
        Utils.sendMSG(Bukkit.getConsoleSender(), msg);
    }
    public void debug(String... msg){
        Arrays.stream(msg).forEach(s-> Utils.sendMSG(Bukkit.getConsoleSender(), "&a[DEBUG] &f• &e"+s));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
