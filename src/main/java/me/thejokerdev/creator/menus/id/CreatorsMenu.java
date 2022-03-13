package me.thejokerdev.creator.menus.id;

import me.thejokerdev.creator.Creator;
import me.thejokerdev.creator.Main;
import me.thejokerdev.creator.menus.Button;
import me.thejokerdev.creator.menus.Menu;
import me.thejokerdev.creator.menus.SimpleItem;
import me.thejokerdev.creator.utils.ItemsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CreatorsMenu extends Menu {
    private int page = 0;
    public int maxItemsPerPage = 15;

    private Button creator;
    private Button prev;
    private Button next;

    private Button anyCreator;

    public CreatorsMenu(Player var1){
        super(var1, "menu");
        updateLang();
    }

    @Override
    public void onOpen(InventoryOpenEvent var1) {
        update();
    }

    @Override
    public void onClose(InventoryCloseEvent var1) {

    }

    @Override
    public void onClick(InventoryClickEvent var1) {
        
    }

    @Override
    public void update() {
        for (Button b : buttons){
            setItem(b);
        }

        Creator[] var1 = Main.getPlugin().getCreatorManager().getCreators();
        List<Creator> arenas = Arrays.asList(var1);

        int index0 = this.page * this.maxItemsPerPage;
        int index1 = Math.min(index0+this.maxItemsPerPage, arenas.size());
        int maxPages = (int)Math.round(Math.ceil((double) arenas.size()/(double)maxItemsPerPage));
        List<SimpleItem> creatorItems = arenas.subList(index0, index1).stream().map(this::getItem).collect(Collectors.toList());

        if (page!=0){
            setItem(prev.getSlot(), prev.getItem());
        }
        if (page+1 < maxPages){
            setItem(next.getSlot(), next.getItem());
        }
        int slotBox = 11;
        if (creatorItems.size() > 0){
            for (SimpleItem var4 : creatorItems) {
                if (slotBox == 16 || slotBox == 25) {
                    slotBox = slotBox + 4;
                }
                this.setItem(slotBox, var4);
                slotBox++;
            }
        } else {
            setItem(anyCreator.getSlot(), anyCreator.getItem());
        }

    }

    private SimpleItem getItem(Creator creator){
        SimpleItem item = this.creator.getItem().clone();
        item = ItemsManager.setPlaceHolders(item, getPlayer());

        item.addPlaceholder("{creator}", creator.getName());
        item.addPlaceholder("{votes}", creator.getVotes()+"");
        item.addPlaceholder("{lastVote}", creator.getLastVote().toString());

        if (item.hasLore()) {
            List<String> lore = new ArrayList<>();
            for (String s : item.getLore()){
                if (s.contains("%line%")){
                    String[] var1 = s.split("%line%");
                    for (String s2 : var1){
                        s2 = s2.replace("%line%", "");
                        lore.add(s2);
                    }
                    continue;
                }
                lore.add(s);
            }
            item.setLore(lore);
        }
        return item;
    }

    @Override
    public void updateLang() {
        if (getConfig().get("extra-items")!=null){
            buttons = new ArrayList<>();
            for (String s : getConfig().getSection("extra-items").getKeys(false)){
                s = "extra-items."+s;
                buttons.add(new Button(getPlayer(), getConfig(), s));
            }
        }
        creator = new Button(getPlayer(), getConfig(), "items.creator");
        next = new Button(getPlayer(), getConfig(), "items.next_page");
        prev = new Button(getPlayer(), getConfig(), "items.prev_page");

        anyCreator = new Button(getPlayer(), getConfig(), "items.anyCreator");
    }
}
