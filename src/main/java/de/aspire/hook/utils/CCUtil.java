package de.aspire.hook.utils;

import org.bukkit.ChatColor;

public class CCUtil {

    public static String translateBoolean(boolean bool){
        return (!bool ? ChatColor.RED : ChatColor.GREEN) + String.valueOf(bool);
    }

}
