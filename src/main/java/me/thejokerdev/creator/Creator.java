package me.thejokerdev.creator;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.scheduler.BukkitRunnable;

import java.security.cert.CertificateParsingException;
import java.util.Date;

@Getter
@Setter
public class Creator {
    private Main plugin;
    private String name;
    private int votes;
    private Date lastVote;

    public Creator(String name){
        this.name = name;
        votes = 0;
        lastVote = new Date();
    }

    public Creator(String name, Main plugin){
        this.name = name;
        votes = 0;
        lastVote = new Date();
        this.plugin = plugin;
    }

    public void loadData (boolean async){
        if (async){
            new BukkitRunnable(){
                @Override
                public void run() {
                    plugin.getDataManager().getData().getCData(Creator.this);
                }
            };
        } else {
            plugin.getDataManager().getData().getCData(this);
        }
    }
    public void syncData(boolean async){
        if (async){
            new BukkitRunnable(){
                @Override
                public void run() {
                    plugin.getDataManager().getData().syncCData(Creator.this);
                }
            };
        } else {
            plugin.getDataManager().getData().syncCData(this);
        }
    }

}
