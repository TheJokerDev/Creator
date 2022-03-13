package me.thejokerdev.creator;

import java.util.Date;
import java.util.HashMap;

public class CreatorsManager {
    private Main plugin;
    private HashMap<String, Creator> creators;

    public CreatorsManager(Main plugin){
        this.plugin = plugin;
    }

    public void init(){
        creators = new HashMap<>();

        for (String s : plugin.getDataManager().getData().getCreators()){
            creators.put(s, new Creator(s));
        }

        for (Creator c : creators.values()){
            c.loadData(true);
        }
    }

    public Creator getCreator(String id) {
        return creators.getOrDefault(id, null);
    }

    public void updateCreator(String creator, int votes, String date){
        Creator creator1 = getCreator(creator);
        if (creator1 == null){
            return;
        }

        creator1.setVotes(votes);
        creator1.setLastVote(new Date(date));
    }

    public Creator[] getCreators() {
        return creators.values().toArray(new Creator[0]);
    }
}
