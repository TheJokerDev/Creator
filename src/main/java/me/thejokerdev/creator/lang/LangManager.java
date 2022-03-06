package me.thejokerdev.creator.lang;

import me.thejokerdev.creator.Main;

import java.io.File;
import java.util.HashMap;

public class LangManager {
    private final Main plugin;
    private HashMap<String, Lang> languages;
    private final File folder;
    private String langDefault;

    public LangManager(Main plugin){
        this.plugin = plugin;
        folder = new File(plugin.getDataFolder(), "lang/");
    }

    public void load(){
        languages = new HashMap<>();
        langDefault = plugin.getConfig().getString("settings.lang");
        if (!folder.exists()){
            folder.mkdir();
            plugin.saveResource("lang/es_MX.yml", false);
        }
        if (folder.listFiles().length > 0){

        }
    }


}
