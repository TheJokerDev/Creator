package me.thejokerdev.creator.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class Utils {
    public static String ct (String msg){
        return ChatColor.translateAlternateColorCodes('&', msg);
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
}
