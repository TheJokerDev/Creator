package me.thejokerdev.creator.data;

import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.data.types.MDBData;
import me.thejokerdev.creator.data.types.MySQLData;
import me.thejokerdev.creator.data.types.SQLiteData;
import me.thejokerdev.creator.data.types.YamlData;

public class DataManager {
    private Data data;
    private final Main plugin;

    public DataManager (Main plugin){
        this.plugin = plugin;
    }

    public DataManager initData(){
        String str = plugin.getConfig().getString("settings.data.type", "yml");
        switch (str.toLowerCase()){
            case "mongodb":
            case "mongo":
            case "mongodatabase":{
                data = new MDBData(plugin);
                break;
            }
            case "sqlite":
            case "sql": {
                data = new SQLiteData(plugin);
                break;
            }
            case "mariadb":
            case "mysql":{
                data = new MySQLData(plugin);
                break;
            }
            case "yml":
            case "yaml":
            default:{
                data = new YamlData(plugin);
                break;
            }
        }
        data.setup();
        return this;
    }

    public Data getData() {
        return data;
    }
}
