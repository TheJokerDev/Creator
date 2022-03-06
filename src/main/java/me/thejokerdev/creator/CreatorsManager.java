package me.thejokerdev.creator;

import java.util.HashMap;

public class CreatorsManager {
    private Main plugin;
    private HashMap<String, Creator> creators;

    public CreatorsManager(Main plugin){
        this.plugin = plugin;
    }

    public void init(){
        creators = new HashMap<>();

    }
}
