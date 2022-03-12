package me.thejokerdev.creator.data;


import me.thejokerdev.creator.Creator;
import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.player.CPlayer;

import java.util.List;

public abstract class Data {
    public Main plugin;
    public Data(Main plugin){
        this.plugin = plugin;
    }
    public abstract DataType getType();

    public abstract void syncData(CPlayer var);
    public abstract void getData(CPlayer var);

    public abstract void syncCData(Creator var);
    public abstract void getCData(Creator var);
    public abstract List<String> getCreators();

    public abstract void reload();

    public abstract boolean isRunning();

    public abstract void setup();
}
