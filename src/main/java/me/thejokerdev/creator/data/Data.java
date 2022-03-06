package me.thejokerdev.creator.data;


import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.player.CPlayer;

public abstract class Data {
    public Main plugin;
    public Data(Main plugin){
        this.plugin = plugin;
    }
    public abstract DataType getType();

    public abstract void syncData(CPlayer var);
    public abstract void getData(CPlayer var);

    public abstract void reload();

    public abstract boolean isRunning();

    public abstract void setup();
}
