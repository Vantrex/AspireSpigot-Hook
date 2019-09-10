package de.aspire.hook.commands;


import de.aspire.hook.AspireSpigotHook;
import org.bukkit.Bukkit;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.util.*;

public abstract class HookCommand implements CommandExecutor, TabExecutor{

    private static Map<HookCommand,Command> registerd = new HashMap<>();

    private final String name;
    private String permission;
    private final List<String> aliases;
    private final String description;


    private boolean active = false;

    private static CommandMap commandMap;



    final CommandMap getCommandMap() {
        if (commandMap == null) {
            try {
                final Field f = Bukkit.getServer().getClass().getDeclaredField("commandMap");
                f.setAccessible(true);
                commandMap = (CommandMap) f.get(Bukkit.getServer());
                return getCommandMap();
            } catch (Exception e) { e.printStackTrace(); }
        } else if (commandMap != null) { return commandMap; }
        return getCommandMap();
    }

    public void setPermissions(final String permissions){
        permission = permissions;
    }

    private void register(){


        this.active = true;

        ReflectCommand cmd = new ReflectCommand(getName());
        if (this.aliases != null) cmd.setAliases(this.aliases);
        if (this.description != null) cmd.setDescription(this.description);
        if (this.permission != null) cmd.setPermissionMessage(this.permission);
        getCommandMap().register("",cmd);
        cmd.setExecutor(this);
        registerd.put(this,cmd);
    }
    public HookCommand(String commandName,List<String> aliases, String permission, String description) {
        String usage1;
        this.name = commandName;
        this.permission = permission;
        this.aliases = aliases;
        this.description = description;
        register();
    }
    public HookCommand(String commandName, List<String> aliases, String permission) {

        this.name = commandName;
        this.permission = permission;
        this.aliases = aliases;
        this.description = "";
        register();
    }
    public HookCommand(String commandName, String permission) {
        this.name = commandName;
        this.permission = permission;
        this.aliases = new ArrayList<String>();
        description = "";
        register();
    }

    public boolean isActive() {
        return active;
    }

    public String getName() {
        return this.name;
    }


    public List<String> getAliases() {
        return this.aliases;
    }

    public abstract void execute(CommandSender sender, String label, String[] args);


    private final class ReflectCommand extends Command {
        private HookCommand exe = null;
        protected ReflectCommand(String command) { super(command); }
        public void setExecutor(HookCommand exe) { this.exe = exe; }
        @Override public boolean execute(CommandSender sender, String commandLabel, String[] args) {
            if (exe != null) { return exe.onCommand(sender, this, commandLabel, args); }
            return false;
        }

        @Override  public List<String> tabComplete(CommandSender sender, String alais, String[] args) {
            if (exe != null) { return exe.onTabComplete(sender, this, alais, args); }
            return null;
        }
    }

    public String getPermission() {
        return permission;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!cmd.getLabel().equalsIgnoreCase(name))
            return true;

        if(permission != null) {
            if ((!sender.hasPermission(permission))) {
                if(!permission.equals("")) {
                    sender.sendMessage();
                    return true;
                }
            }
        }
        execute( sender,label,args);
        return true;
    }

    public static void unregisterCommands(){
        registerd.forEach((HookCommand, command) -> {
            unregisterCommand(HookCommand);
        });
    }

    public static void unregisterCommand(HookCommand command){

        Command tempCommand = registerd.get(command);
        command.active = false;
        command.getCommandMap().getCommand(command.getName()).unregister(command.getCommandMap());

        String label = tempCommand.getLabel();
        List<String> aliases = tempCommand.getAliases();
        try{
            final Field f = commandMap.getClass().getDeclaredField("knownCommands");
            f.setAccessible(true);
            Map<String, Command> cmds = (Map<String, Command>) f.get(commandMap);
            cmds.remove(label);
            aliases.forEach(cmds::remove);
            f.set(commandMap,cmds);
        }catch (Exception e){
            e.printStackTrace();
        }
        registerd.remove(command);
        if(!tempCommand.isRegistered()){
            System.out.println("Command " + tempCommand.getName() + " unregisterd");
        }
    }


    public void setPermission(String permission) {
        this.permission = permission;
    }
    public static HookCommand getHookCommandFromName(String name){
        Map<String,HookCommand> temp = new HashMap<>();
        name = name.toLowerCase();
        AspireSpigotHook.getInstance().getCommandManager().getCommands().forEach((s, HookCommand) -> temp.put(s.toLowerCase(),HookCommand));

        return temp.getOrDefault(name, null);
    }

}
