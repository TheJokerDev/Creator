package me.thejokerdev.creator.menus;

import lombok.Getter;
import me.thejokerdev.creator.utils.FileUtils;
import me.thejokerdev.creator.utils.ItemsManager;
import me.thejokerdev.creator.utils.Utils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

@Getter
public class Button {
    private final SimpleItem item;
    private final List<Integer> slot;
    private final Player player;
    private final ConfigurationSection section;
    private final FileUtils file;
    private int cooldown = 0;


    public Button(Player player, FileUtils file, String section){
        this.file = file;
        this.player = player;
        this.item = ItemsManager.createItem(player, file.getSection(section), null);
        this.slot = getSlotFromString(file.getSection(section).getString("slot"));
        this.section = file.getSection(section);
        if (this.section.get("cooldown")!=null){
            this.cooldown = this.section.getInt("cooldown");
        }
    }

    private List<Integer> getSlotFromString(String var1){
        if (var1 == null){
            return new ArrayList<>(Collections.singletonList(0));
        }
        boolean isOne = !var1.contains(",") && !var1.contains("-");
        List<Integer> slots = new ArrayList<>();
        if (isOne){
            try {
                int i = Integer.parseInt(var1);
                slots.add(i);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            String[] var2 = new String[0];
            if (var1.contains(",")){
                var2 = var1.split(",");
            } else if (var1.contains("-")){
                var2 = var1.split("-");
                for (int i = Integer.parseInt(var2[0]); i < Integer.parseInt(var2[1]); i++){
                    slots.add(i);
                }
                return slots;
            }
            for (String s : var2){
                slots.addAll(getSlotFromString(s));
            }
        }
        return slots;
    }

    public Button(Player player, FileUtils file, String section, HashMap<String, String> pl){
        this.player = player;
        this.file = file;
        this.item = ItemsManager.createItem(player, file.getSection(section), pl);
        this.slot = getSlotFromString(file.getSection(section).getString("slot"));
        this.section = file.getSection(section);
        if (this.section.get("cooldown")!=null){
            this.cooldown = this.section.getInt("cooldown");
        }
    }

    public int getCooldown() {
        return cooldown;
    }

    public void executePhysicallyItemsActions(PlayerInteractEvent e){
        e.setCancelled(true);
        if (section.get("actions")==null){
            return;
        }
        List<String> leftClick = section.getStringList("actions.leftclick");
        List<String> rightClick = section.getStringList("actions.rightclick");
        List<String> shiftClick = section.getStringList("actions.shiftclick");
        List<String> all = section.getStringList("actions.multiclick");

        if (!leftClick.isEmpty() && e.getAction().name().contains("LEFT")){
            Utils.actions(getPlayer(), leftClick);
        }
        if (!rightClick.isEmpty() && e.getAction().name().contains("RIGHT")){
            Utils.actions(getPlayer(), rightClick);
        }
        if (!shiftClick.isEmpty() && e.getPlayer().isSneaking()){
            Utils.actions(getPlayer(), shiftClick);
        }
        if (!all.isEmpty()){
            Utils.actions(getPlayer(), all);
        }
    }

    public void executeItemInMenuActions(InventoryClickEvent e){
        if (section.get("actions")==null){
            return;
        }
        List<String> leftClick = section.getStringList("actions.leftclick");
        List<String> rightClick = section.getStringList("actions.rightclick");
        List<String> middleClick = section.getStringList("actions.middleclick");
        List<String> shiftClick = section.getStringList("actions.shiftclick");
        List<String> all = section.getStringList("actions.multiclick");

        if (!leftClick.isEmpty() && e.getClick() == ClickType.LEFT){
            Utils.actions(getPlayer(), leftClick);
        }
        if (!rightClick.isEmpty() && e.getClick() == ClickType.RIGHT){
            Utils.actions(getPlayer(), rightClick);
        }
        if (!middleClick.isEmpty() && e.getClick() == ClickType.MIDDLE){
            Utils.actions(getPlayer(), middleClick);
        }
        if (!shiftClick.isEmpty() && e.getClick().name().contains("SHIFT")){
            Utils.actions(getPlayer(), shiftClick);
        }
        if (!all.isEmpty()){
            Utils.actions(getPlayer(), all);
        }
    }
}
