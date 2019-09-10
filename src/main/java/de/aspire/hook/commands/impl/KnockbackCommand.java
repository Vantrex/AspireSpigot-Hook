package de.aspire.hook.commands.impl;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IProfileHook;
import de.aspire.hook.commands.HookCommand;
import de.aspire.hook.gui.InventoryGui;
import de.aspire.hook.other.ItemBuilder;
import de.aspire.hook.profile.knockback.KnockbackProfileHook;
import de.aspire.hook.utils.AnvilGUI;
import de.aspire.hook.utils.CCUtil;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.manager.impl.KnockbackManager;
import de.aspire.spigot.profiles.knockback.KnockbackProfile;
import de.aspire.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class KnockbackCommand extends HookCommand{

    private final Map<String,Object> settings;

    public KnockbackCommand(String commandName, List<String> aliases, String permission, String description) {
        super(commandName, aliases, permission, description);

        settings = new HashMap<>();

        settings.put("friction",Boolean.class);
        settings.put("sprintReset",Boolean.class);
        settings.put("airKB",Boolean.class);
        settings.put("horizontal",Double.class);
        settings.put("vertical",Double.class);
        settings.put("hitDelay",Integer.class);
        settings.put("airHorizontal",Double.class);
        settings.put("airVertical",Double.class);
        settings.put("frictionValue",Double.class);
        settings.put("verticalLimit",Double.class);
        settings.put("name",String.class);

    }

    @Override
    public void execute(CommandSender sender, String label, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can perform this command!");
            return;
        }

        if(!sender.hasPermission(AspireSpigotHook.getInstance().getHookConfig().getPermission("command.knockback"))){
            sender.sendMessage(AspireSpigotHook.getInstance().getHookConfig().getMessage("no-perm-message"));
            return;
        }

        /*
            We could use an extra argument class, but i think we don´t need one.
         */

        Player player = (Player) sender;

        if (args.length == 1 && (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("menü"))) {
            /* Open menu */
            mainKnockbackGui(player);
        } else if (args.length == 1 && (args[0].equalsIgnoreCase("toggle"))) {
            /* Toggle default kb */
            KnockbackManager.DEFAULT_KB = !KnockbackManager.DEFAULT_KB;
            player.sendMessage("§7Vanilla kb toggled to " + CCUtil.translateBoolean(KnockbackManager.DEFAULT_KB));
        } else if(args.length == 4 && args[0].equalsIgnoreCase("set")){

            /* Change kb settings of a certain profile */

            if(!AspireSpigotHook.getInstance().getKnockbackHookManager().isValidProfile(args[1])){
                player.sendMessage("§cInvalid profile! Case intensive?");
                return;
            }
            KnockbackProfileHook knockbackProfileHook = (KnockbackProfileHook) AspireSpigotHook.getInstance().getKnockbackHookManager().getProfile(args[1]);
            // player.sendMessage(" §7/kb §bset <profile> <key> <value>");

            List<String> temp = new ArrayList<>();
            settings.keySet().forEach(s -> temp.add(s.toLowerCase())); // cant be replaced with temp.addAll(settings.KeySet()) because we need it lowerCase

            if(!temp.contains(args[2].toLowerCase())){
                player.sendMessage("§cCan´t find key with the name " + args[2].toLowerCase());
                return;
            }

            String key = args[2].toLowerCase();

            String value = args[3];

            if(key.equalsIgnoreCase("friction") && isBoolean(value)){
                knockbackProfileHook.setFriction(Boolean.parseBoolean(value));
            }else if(key.equalsIgnoreCase("frictionValue") && isDouble(value)){
                knockbackProfileHook.setFrictionValue(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("horizontal") && isDouble(value)){
                knockbackProfileHook.setHorizontal(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("vertical") && isDouble(value)){
                knockbackProfileHook.setVertical(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("airHorizontal") && isDouble(value)){
                knockbackProfileHook.setAirHorizontal(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("airVertical") && isDouble(value)){
                knockbackProfileHook.setAirVertical(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("airKB") && isBoolean(value)){
                knockbackProfileHook.setAirKB(Boolean.valueOf(value));
            }else if(key.equalsIgnoreCase("sprintReset") && isBoolean(value)){
                knockbackProfileHook.setSprintReset(Boolean.parseBoolean(value));
            }else if(key.equalsIgnoreCase("verticalLimit") && isDouble(value)){
                knockbackProfileHook.setVerticalLimit(Double.parseDouble(value));
            }else if(key.equalsIgnoreCase("hitDelay") && isInteger(value)){
                knockbackProfileHook.setHitDelay(Integer.parseInt(value));
            }else if(key.equalsIgnoreCase("name")){
                knockbackProfileHook.setName(value);
            }else{
                player.sendMessage("§cCant assign value to key.");
                return;
            }
            player.sendMessage("§7Changed value from §5" + key + " §7to §5" + value + "§7!");
            knockbackProfileHook.applyChanges();

        } else if ((args.length == 2 || args.length == 3) && args[0].equalsIgnoreCase("bind")) {
            /* Bind knockback profile */

            boolean setPlayerProfile = args.length == 3;
            /* Get profileName */
            String profileName = setPlayerProfile ? args[2] : args[1];
            if(!AspireSpigotHook.getInstance().getKnockbackHookManager().isValidProfile(profileName)){
                player.sendMessage("§cInvalid profile! Case intensive?");
                return;
            }

            if(setPlayerProfile){
                Player target = Bukkit.getPlayer(args[1]);
                if(target == null || !target.isOnline()){
                    player.sendMessage("§cTarget is not online!");
                    return;
                }

                target.setKnockbackProfile((KnockbackProfile) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getKnockbackHookManager().getProfile(profileName)));
                player.sendMessage("§7Knockbackprofile of §5" + target.getName() + " §7set to §5" + player.getKnockbackProfile().getName());
            }else{
                AspireSpigot.getInstance().getKnockbackManager().setActiveProfile((KnockbackProfile) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getKnockbackHookManager().getProfile(profileName)));
                player.sendMessage("§7Server KnockbackProfile set to §5" + profileName);
            }

        } else if (args.length == 2 && args[0].equalsIgnoreCase("check")) {
            /* Check Knockback profile */
            if (args[1].equalsIgnoreCase("server")) {
                sendSettings(player, (KnockbackProfileHook) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromSpigotProfile(AspireSpigot.getInstance().getKnockbackManager().getActiveProfile()));
            } else if (Bukkit.getPlayer(args[1]) != null) {
                sendSettings(player, (KnockbackProfileHook) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromSpigotProfile(Bukkit.getPlayer(args[1]).getKnockbackProfile()));
            } else {
                if(!AspireSpigotHook.getInstance().getKnockbackHookManager().isValidProfile(args[1])){
                    player.sendMessage("§cInvalid profile! Case intensive?");
                    sendHelp(player);
                    return;
                }
                sendSettings(player, (KnockbackProfileHook) AspireSpigotHook.getInstance().getKnockbackHookManager().getProfile(args[1]));
            }
        }else{
            /* Send help */
            sendHelp(player);
        }

    }

    private void sendSettings(Player receiver, KnockbackProfileHook knockbackProfileHook){
        if(knockbackProfileHook == null) receiver.sendMessage("Kb profile = null");
        receiver.sendMessage(Messages.getHeader("§b§l","KNOCKBACK"));
        receiver.sendMessage(" ");
        receiver.sendMessage("§7Name: §b" + knockbackProfileHook.getName());
        receiver.sendMessage("§7Sprint Reset: §b" + CCUtil.translateBoolean(knockbackProfileHook.isSprintReset()));
        receiver.sendMessage("§7Friction: §b" + (knockbackProfileHook.isFriction() ? String.valueOf(knockbackProfileHook.getFrictionValue()) : CCUtil.translateBoolean(knockbackProfileHook.isFriction())));
        receiver.sendMessage("§7Horizontal: §b" + knockbackProfileHook.getHorizontal());
        receiver.sendMessage("§7Vertical: §b" + knockbackProfileHook.getVertical());

        if(knockbackProfileHook.isFriction()){
            receiver.sendMessage("§7Extra Horizontal: §b" + knockbackProfileHook.getAirHorizontal());
            receiver.sendMessage("§7Extra Vertical: §b" + knockbackProfileHook.getAirVertical());
        }else{
            receiver.sendMessage("§7Air KB: " + CCUtil.translateBoolean(knockbackProfileHook.isAirKB()));
            if(knockbackProfileHook.isAirKB()){
                receiver.sendMessage("§7Air Horizontal: §b"  + knockbackProfileHook.getAirHorizontal());
                receiver.sendMessage("§7Air Vertical: §b" + knockbackProfileHook.getAirVertical());
            }
        }
        receiver.sendMessage("§7Hit Delay: §b" + knockbackProfileHook.getHitDelay());
        receiver.sendMessage(" ");
        receiver.sendMessage(Messages.getFooter("KNOCKBACK"));

    }

    private void sendHelp(Player player){
        player.sendMessage(Messages.getHeader("§b§l","KNOCKBACK"));
        player.sendMessage(" ");
        player.sendMessage(" §7/kb §bmenu §8- §7open a simple gui.");
        player.sendMessage(" §7/kb §btoggle §8- §7Toggle between the vanilla & custom kb.");
        player.sendMessage(" §7/kb §bbind <player> <profile> §8- §7Bind´s a kb profile to a player, null=active kb.");
        player.sendMessage(" §7/kb §bbind <profile> §8- §7Set the active kb.");
        player.sendMessage(" §7/kb §bcheck <server,player, profile> §8- §7Get current profile of player/server.");
        player.sendMessage(" §7/kb §bset <profile> <key> <value>");
        player.sendMessage(" ");
        player.sendMessage(Messages.getFooter("KNOCKBACK"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) return null;
        if(!sender.hasPermission(AspireSpigotHook.getInstance().getHookConfig().getPermission("command.knockback"))) return null;
       if(args.length > 0){
           return returnTabComplete(args, args[args.length - 1]);
       }else{
           return null;
       }
    }

    private List<String> returnTabComplete(String args[], String chars){

        if(args.length == 1) {
            List<String> list = Arrays.asList("menu", "bind", "toggle", "check","set");
            List<String> re = new ArrayList<>();
            re.addAll(list.stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList()));
            return re;
        }else if(args.length == 2 && args[0].equalsIgnoreCase("bind")) {
            List<String> re = new ArrayList<>();
            re.add("server");
            Bukkit.getOnlinePlayers().forEach(player -> {
                if (player.getName().toLowerCase().startsWith(chars.toLowerCase())) {
                    re.add(player.getName());
                }
            });
            return re;
        }else if(args.length == 3 && args[0].equalsIgnoreCase("bind")) {
            return AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("check")) {
            List<String> list = new ArrayList<>();
            list.add("server");
            list.addAll(AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().keySet());


            Bukkit.getOnlinePlayers().forEach(player -> {
                list.add(player.getName());
            });
            return list.stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
            //  player.sendMessage(" §7/kb §bset <profile> <key> <value>");
        }else if(args.length == 2 && args[0].equalsIgnoreCase("set")){
            return AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 3 && args[0].equalsIgnoreCase("set")){
            return settings.keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }
        else{
            return null;
        }
    }

    private void mainKnockbackGui(Player player){
        InventoryGui gui = AspireSpigotHook.getInstance().gui(27).setName("§d§lKnockback").build();
        gui.setItem(10, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.CHEST).setDisplayName("§6Profiles").build(), this::knockbackListGui));
        gui.setItem(13, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.SIGN).setDisplayName("§5Info").setLore(Arrays.asList("§7DefaultKB: " + CCUtil.translateBoolean(KnockbackManager.DEFAULT_KB),"§7Active: §b" + AspireSpigot.getInstance().getKnockbackManager().getActiveProfile().getName())).build(), (Consumer<Player>) null));
        gui.setItem(16, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.WORKBENCH).setDisplayName("§6Create").setLore(Collections.singletonList("§7Create a profile")).build(), this::createProfile));
        gui.open(player);
        gui.setDestroyOnClose(true);
    }

    private void knockbackListGui(Player player){

        InventoryGui gui = AspireSpigotHook.getInstance().gui(54).setName("§d§lKnockback list").build();

        int i = 0;

        for(IProfileHook iProfileHook : AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().values()){
            /* This should not happen but we´re catching it anyways */
            if(!(iProfileHook instanceof KnockbackProfileHook)) continue;

            if(i > gui.getSize()) break;

            gui.setItem(i, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.DIAMOND_SWORD).setDisplayName("§b" + iProfileHook.getName()).build(), player1 -> openProfile(player1, (KnockbackProfileHook) iProfileHook)));
            i++;
        }
        gui.open(player);
        gui.setDestroyOnClose(true);
    }

    private void openProfile(Player player, KnockbackProfileHook knockbackProfileHook){

       player.closeInventory();
       sendSettings(player,knockbackProfileHook);
    }

    private boolean isBoolean(String string){
        return string.equalsIgnoreCase("false") || string.equalsIgnoreCase("true");
    }

    private boolean isInteger(String string){
        try {
            Integer.parseInt(string);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private boolean isDouble(String string){
        try {
            Double.parseDouble(string);
        }catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    private void createProfile(final Player player){


        player.closeInventory();

        AnvilGUI anvilGUI = new AnvilGUI(player, new AnvilGUI.AnvilClickEventHandler() {
            @Override
            public void onAnvilClick(AnvilGUI.AnvilClickEvent event) {

                if(event.getName().contains(" ")){
                    event.setWillClose(false);
                    event.setWillDestroy(false);
                    player.sendMessage("§cInvalid profile name!");
                    return;
                }

                if(AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().containsKey(event.getName())){
                    event.setWillDestroy(false);
                    event.setWillClose(false);
                    player.sendMessage("§cProfile with this name already exists!");
                    return;
                }
                KnockbackProfile knockbackProfile = new KnockbackProfile(AspireSpigot.getInstance(),event.getName());
                AspireSpigot.getInstance().getKnockbackManager().add(knockbackProfile);
                AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().put(knockbackProfile.getName(), new KnockbackProfileHook(knockbackProfile.getName()));
                event.setWillDestroy(true);
                event.setWillClose(true);
                player.sendMessage("§7KnockbackProfile §b" + event.getName() + " §7addded!");
            }
        });

        anvilGUI.setSlot(AnvilGUI.AnvilSlot.INPUT_LEFT,AspireSpigotHook.getInstance().item(Material.FEATHER).setDisplayName("profile name").build());
        anvilGUI.open();


    }
}
