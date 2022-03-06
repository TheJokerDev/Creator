package me.thejokerdev.creator;

import lombok.Getter;
import lombok.Setter;
import me.thejokerdev.creator.data.DataManager;
import me.thejokerdev.creator.lang.LangManager;
import me.thejokerdev.creator.player.PlayerManager;
import me.thejokerdev.creator.utils.Hastebin;
import me.thejokerdev.creator.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

@Getter
@Setter
public final class Main extends JavaPlugin {
    private static Main plugin;
    private LangManager langManager;
    private DataManager dataManager;
    private PlayerManager playerManager;
    private CreatorsManager creatorManager;

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


        ms = System.currentTimeMillis()-ms;
        console(" &aPlugin correctly loaded in "+ms+" ms.",
                "&f&m ======================================================== ");

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
                "    &cCause: "+e.getCause().getLocalizedMessage(),
                "",
                "    &cContact with &bTheJokerDev&c and give this link:",
                "    &cFull error: "+hastebin.paste(e.toString()),
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
