package de.aspire.hook.utils.file.impl;

import de.aspire.hook.utils.file.FileBase;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HookConfig extends FileBase{

    private final Map<String,String> messages;
    private final Map<String,String> permissions;


    public HookConfig() {
        super("config.yml");
        this.messages = new HashMap<>();
        this.permissions = new HashMap<>();
        init();
    }

    private void init(){

        FileConfiguration cfg = getConfig();
        cfg.addDefault("no-perm-message","&cDu hast für diesen Befehl keine Rechte.");
        cfg.addDefault("unknown-command","&cDieser Befehl wurde nicht gefunden.");
        cfg.addDefault("permissions.knockback-command","aspire.knockback");
        cfg.addDefault("permissions.potions-command","aspire.potions");
        try {
            cfg.save(getFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadMessages();
        loadPermissions();
    }

    private void loadPermissions(){
        FileConfiguration cfg = getConfig();
        permissions.put("command.knockback",cfg.getString("permissions.knockback-command"));
        permissions.put("command.potions",cfg.getString("permissions.potions-command"));
    }

    private void loadMessages(){
        FileConfiguration cfg = getConfig();
        messages.put("no-perm",cfg.getString("no-perm-message"));
        messages.put("unknown-command",cfg.getString("unknown-command"));
    }

    public String getPermission(String permission){
        return permissions.getOrDefault(permission,"*");
    }

    public String getMessage(String message){
        return ChatColor.translateAlternateColorCodes('&',messages.getOrDefault(message,"NOT_FOUND"));
    }

}
