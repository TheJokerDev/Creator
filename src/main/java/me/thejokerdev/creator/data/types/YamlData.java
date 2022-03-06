package me.thejokerdev.creator.data.types;

import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.data.Data;
import me.thejokerdev.creator.data.DataType;
import me.thejokerdev.creator.player.CPlayer;
import me.thejokerdev.creator.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class YamlData extends Data {
    private FileUtils fileUtils;
    private boolean running;

    public YamlData(Main plugin) {
        super(plugin);
    }

    @Override
    public DataType getType() {
        return DataType.YAML;
    }

    @Override
    public void syncData(CPlayer var) {
        String name = var.getName();
        Date date = var.getDateOfInteraction();
        boolean interacted = var.isInteracted();

        fileUtils.set(name+".date", date.toString());
        fileUtils.set(name+".interacted", interacted);
        fileUtils.save();
        fileUtils.reload();
    }

    @Override
    public void getData(CPlayer var) {
        if (fileUtils.get(var.getName())==null){
            fileUtils.add(var.getName()+".uuid", var.getUniqueId().toString());
            fileUtils.add(var.getName()+".date", "");
            fileUtils.add(var.getName()+".interacted", true);
            fileUtils.save();
            fileUtils.reload();
        }

        var.setDateOfInteraction(new Date(fileUtils.getString(var.getName()+".date")));
        var.setInteracted(fileUtils.getBoolean(var.getName()+".interacted"));
    }

    @Override
    public void reload() {
        setup();
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public void setup() {
        File file = new File(plugin.getDataFolder(), plugin.getConfig().getString("settings.data.yaml.file"));
        if (!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException ignored) {
            }
        }
        fileUtils = new FileUtils(file);

        running = true;
    }
}
