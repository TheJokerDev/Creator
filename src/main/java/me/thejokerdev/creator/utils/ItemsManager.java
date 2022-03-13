package me.thejokerdev.creator.utils;

import lombok.Getter;
import me.clip.placeholderapi.PlaceholderAPI;
import me.thejokerdev.creator.menus.SimpleItem;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkEffectMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;


@Getter
public class ItemsManager {

    public static SimpleItem createItem(Player player, ConfigurationSection section, HashMap<String, String> placeholders){
        int int1 = 0;
        int int2 = 0;
        int int3 = 0;
        boolean hasMeta = section.get("meta")!=null;
        boolean hasName = section.get("meta.name")!=null;
        boolean hasAmount = section.get("amount")!=null;
        boolean hasLore = section.get("meta.lore")!=null;
        boolean hasSkullData = section.get("skull")!= null;
        boolean isGlow = section.get("glow")!= null;
        boolean hideFlags = section.get("hideFlags")!= null;
        boolean hasFireWork = section.get("firework")!= null;
        boolean hasPotion = section.get("potion")!= null;
        boolean hasColor = section.get("color")!= null;
        boolean hasMaterial = section.get("material")!= null;
        boolean hasData = section.get("data")!= null;
        boolean hasPlaceholder = section.get("placeholders")!= null;

        SimpleItem item = new SimpleItem(XMaterial.BARRIER);

        if (hasMaterial){
            XMaterial material = XMaterial.valueOf(section.getString("material").toUpperCase());
            item.setMaterial(material);
            item.setDurability(material.getData());
        }
        if (hasData){
            int data = section.getInt("data");
            item.setDurability(data);
        }
        if (placeholders != null && !placeholders.isEmpty()){
            placeholders.forEach(item::addPlaceholder);
        }
        if (hasPlaceholder){
            List<String> pl = section.getStringList("placeholders");
            for (String s : pl){
                if (s.contains(",")){
                    String[] s2 = s.split(",");
                    String key = s2[0];
                    String value;
                    if (player == null){
                        value = PlaceholderAPI.setPlaceholders(null, s2[1]);
                    } else {
                        value = PlaceholderAPI.setPlaceholders(player.getPlayer(), s2[1]);
                    }

                    item.addPlaceholder(key, value);
                }
            }
        }
        if (hasMeta){
            if (hasName){
                String name = section.getString("meta.name");
                item.setDisplayName(Utils.ct(name));
            }
            if (hasLore){
                List<String> lore = section.getStringList("meta.lore");
                item.setLore(Utils.ct(lore));
            }
        }
        if (hasAmount){
            item.setAmount(section.getInt("amount"));
        }
        if (hasSkullData){
            String skull = section.getString("skull");
            if (player!=null){
                skull = PlaceholderAPI.setPlaceholders(player.getPlayer(), skull);
            }
            item.setSkin(skull);
        }
        if (isGlow){
            item.setGlowing(section.getBoolean("glow"));
        }
        if (hideFlags){
            item.setShowAttributes(section.getBoolean("hideFlags"));
        }
        if (hasFireWork){
            String color;
            String[] var1;
            ItemMeta meta = item.build(player).getItemMeta();
            FireworkEffectMeta metaFw = (FireworkEffectMeta) meta;
            color = section.getString("firework");
            Color color1;
            var1 = color.split("-");
            if (var1.length == 3) {
                int1 = Utils.isNumeric(var1[0]) ? Integer.parseInt(var1[0]) : 0;
                int2 = Utils.isNumeric(var1[1]) ? Integer.parseInt(var1[1]) : 0;
                int3 = Utils.isNumeric(var1[2]) ? Integer.parseInt(var1[2]) : 0;
            }
            color1 = Color.fromRGB(int1, int2, int3);
            FireworkEffect effect = FireworkEffect.builder().withColor(color1).build();
            metaFw.setEffect(effect);
            item.setFireworkEffectMeta(metaFw);
        }
        if (hasColor){
            String color = section.getString("color");
            String[] var1 =color.split("-");
            item.setColor(getColor(var1));
        }
        if (hasPotion){
            String id = section.getString("potion.id").toUpperCase();
            PotionEffectType potEffect = XPotion.valueOf(id).parsePotionEffectType();
            int multiplier = section.getInt("potion.multiplier")-1;
            int time = section.getInt("potion.time")*20;
            boolean showParticles = section.getBoolean("potion.showParticles");
            boolean ambient = section.getBoolean("potion.ambient");
            PotionMeta potionMeta = (PotionMeta)item.getMeta();
            potionMeta.addCustomEffect(new PotionEffect(potEffect, time, multiplier, ambient, showParticles), true);
            item.setMeta(potionMeta);
        }
        return item;
    }

    public static Color getColor(String[] var1a) {
        int int1;
        int int2;
        int int3;
        Color color1;
        if (var1a.length == 3) {
            int1 = Utils.isNumeric(var1a[0]) ? Integer.parseInt(var1a[0]) : 0;
            int2 = Utils.isNumeric(var1a[1]) ? Integer.parseInt(var1a[1]) : 0;
            int3 = Utils.isNumeric(var1a[2]) ? Integer.parseInt(var1a[2]) : 0;
        } else {
            int1 = 0;
            int2 = 0;
            int3 = 0;
        }
        color1 = Color.fromRGB(int1, int2, int3);
        return color1;
    }

    public static SimpleItem createChestItem(String[] split){
        SimpleItem item = null;
        StringBuilder name = new StringBuilder();
        List<String> stringC = new ArrayList<>();
        List<String> loreC = new ArrayList<>();
        List<String> lore = new ArrayList<>();
        boolean nameOrLore = false;
        for (String s : split){
            if (item == null){
                try {
                    int data = 0;
                    int amount = 1;
                    if (s.contains(":")){
                        data = Integer.parseInt(s.split(":")[1]);
                        if (s.split(":").length == 3){
                            try {
                                amount = Integer.parseInt(s.split(":")[2]);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                        }
                        s = s.split(":")[0];
                    }
                    XMaterial mat = XMaterial.matchXMaterial(s.toUpperCase()).get();
                    item = new SimpleItem(mat);
                    if (data == 0){
                        data = mat.getData();
                    }
                    item.setDurability(data);
                    item.setAmount(amount);
                    continue;
                } catch (Exception e) {
                    Utils.sendMSG(null, "&c¡No se pudo encontrar el material: "+s);
                    break;
                }
            }
            boolean isEnchant = s.startsWith("e:");
            boolean isSkull = s.startsWith("s:");
            boolean isGlow = s.startsWith("g:");
            boolean isPotion = s.startsWith("p:");
            boolean isName = s.startsWith("n:");
            boolean isLore = s.startsWith("l:");

            if (isEnchant){
                s = s.replace("e:", "");
                String[] split2 = s.split(",");
                String id = split2[0].toUpperCase();
                int level = Integer.parseInt(split2[1]);

                item.addEnchantment(XEnchantment.matchXEnchantment(id).get(), level);
                continue;
            }
            if (isSkull){
                s = s.replace("s:", "");
                item.setSkin(s);
                continue;
            }
            if (isGlow){
                s = s.replace("g:", "");
                boolean bool = Boolean.parseBoolean(s.toUpperCase());
                item.setGlowing(bool);
                continue;
            }
            if (isPotion){
                s = s.replace("p:", "");
                String[] str = s.split(",");
                String id = str[0].toUpperCase();
                PotionEffectType potEffect = XPotion.valueOf(id).parsePotionEffectType();
                int multiplier = Integer.parseInt(str[1])-1;
                int time = Integer.parseInt(str[2])*20;
                boolean showParticles = false;
                boolean ambient = true;
                PotionMeta potionMeta = (PotionMeta)item.getMeta();
                potionMeta.addCustomEffect(new PotionEffect(potEffect, time, multiplier, ambient, showParticles), true);
                item.setMeta(potionMeta);
                continue;
            }
            if (isName) {
                nameOrLore = true;
            }
            if (isLore){
                nameOrLore = false;
            }

            if (nameOrLore){
                if (s.startsWith("n:")) {
                    s = s.replace("n:", "");
                }
                stringC.add(s);
            } else {
                if (s.startsWith("l:")) {
                    s = s.replace("l:", "");
                    iterator(loreC, lore);
                    loreC.clear();
                }
                loreC.add(s);
            }

        }
        if (item!=null) {
            if (!stringC.isEmpty()){
                Iterator<String> iterator = stringC.stream().iterator();
                while (iterator.hasNext()) {
                    name.append(iterator.next());
                    if (iterator.hasNext()){
                        name.append(" ");
                    }
                }
            }
            if (!loreC.isEmpty()){
                iterator(loreC, lore);
            }
            if (lore.size()>0) {
                lore.remove(0);
            }
            item.setDisplayName(name.toString());
            item.setLore(lore);
        }
        return item;
    }
    private static void iterator(List<String> loreC, List<String> lore) {
        Iterator<String> iterator = loreC.stream().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            sb.append(iterator.next());
            if (iterator.hasNext()){
                sb.append(" ");
            }
        }
        lore.add(sb.toString());
    }

    public static ItemStack setPlaceHolders(ItemStack item, Player p) {
        ItemStack simpleItem = item;
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(PlaceholderAPI.setPlaceholders(p, meta.getDisplayName()));
        List<String> lore;
        if (meta.hasLore()) {
            lore = new ArrayList<>();
            for (String s : meta.getLore()) {
                lore.add(PlaceholderAPI.setPlaceholders(p, s));
            }
        }
        simpleItem.setItemMeta(meta);
        return simpleItem;
    }

    public static SimpleItem setPlaceHolders(SimpleItem item, Player p) {
        SimpleItem simpleItem = item;
        simpleItem.setDisplayName(PlaceholderAPI.setPlaceholders(p, simpleItem.getDisplayName()));
        List<String> lore;
        if (simpleItem.hasLore()) {
            lore = new ArrayList<>();
            for (String s : simpleItem.getLore()) {
                lore.add(PlaceholderAPI.setPlaceholders(p, s));
            }
            simpleItem.setLore(lore);
        }
        return simpleItem;
    }
}
