package me.thejokerdev.creator.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Utils {
    public static String ct (String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
    public static List<String> ct (List<String> list){
        return list.stream().map(Utils::ct).collect(Collectors.toList());
    }

    public static String getMSG(CommandSender sender, String in){
        in = ct(in);



        return in;
    }

    public static void sendMSG(CommandSender sender, String... array){
        Arrays.stream(array).forEach(s->sendMSG(sender, s));
    }

    public static void sendMSG(CommandSender sender, String msg){
        msg = getMSG(sender, msg);


        if (sender instanceof Player){
            sender.sendMessage(msg);
        } else {
            Bukkit.getConsoleSender().sendMessage(msg);
        }
    }
    public static void actions(Player p, List<String> list){
        for (String s : list){
            s = PlaceholderAPI.setPlaceholders(p, s);
            if (s.startsWith("[close]")){
                p.closeInventory();
                continue;
            }
            if (s.startsWith("[sound]")){
                playAudio(p, s.replace("[sound]", ""));
                continue;
            }
            if (s.startsWith("[cmd]")){
                s = s.replace("[cmd]", "");
                p.chat("/"+s);
            }
            if (s.startsWith("[cmdOP]")){
                s = s.replace("[cmdOP]", "");
                Bukkit.dispatchCommand(p, s);
            }
            if (s.startsWith("[console]")){
                s = s.replace("[console]", "");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s);
            }
            if (s.startsWith("[msg]")){
                s = s.replace("[msg]", "");
                s = Utils.ct(s);
                p.sendMessage(s);
            }
            if (s.startsWith("[dialog]")){
                s = s.replace("[dialog]", "");
                //Stuff
            }
        }
    }

    public static void playAudio(Player p, String id){
        XSound sound;
        String[] split = id.split(",");
        try {
            sound = XSound.valueOf(split[0].toUpperCase());
        } catch (IllegalArgumentException e) {
            return;
        }
        float volume = Float.parseFloat(split[1]);
        float pitch = Float.parseFloat(split[2]);
        sound.play(p, volume, pitch);
    }

    public static boolean isNumeric(String var0) {
        try {
            Integer.parseInt(var0);
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }
}
