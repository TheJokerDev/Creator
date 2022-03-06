package me.thejokerdev.creator.player;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

@Getter
@Setter
public class CPlayer {
    private String name;
    private UUID uniqueId;

    private boolean interacted = false;
    private Date dateOfInteraction = null;

    public CPlayer(Player player){
        name = player.getName();
        uniqueId = player.getUniqueId();
    }
}
