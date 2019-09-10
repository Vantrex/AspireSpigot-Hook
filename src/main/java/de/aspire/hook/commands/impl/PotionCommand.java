package de.aspire.hook.commands.impl;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IProfileHook;
import de.aspire.hook.commands.HookCommand;
import de.aspire.hook.gui.InventoryGui;
import de.aspire.hook.other.ItemBuilder;
import de.aspire.hook.profile.pots.PotionProfileHook;
import de.aspire.hook.utils.CCUtil;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.manager.impl.KnockbackManager;
import de.aspire.spigot.profiles.potion.PotionProfile;
import de.aspire.spigot.utils.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PotionCommand extends HookCommand{
    public PotionCommand(String commandName, List<String> aliases, String permission, String description) {
        super(commandName, aliases, permission, description);
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
        final Player player = (Player) sender;

        if(args.length == 1 && (args[0].equalsIgnoreCase("menu") || args[0].equalsIgnoreCase("menü"))) {
            mainPotionGUI(player);
        }else if(args.length == 1 && args[0].equalsIgnoreCase("toggle")) {
            AspireSpigot.getInstance().getPotionManager().setVanilla(!AspireSpigot.getInstance().getPotionManager().isVanilla());
            player.sendMessage("§7Vanilla pots toggled to " + CCUtil.translateBoolean(AspireSpigot.getInstance().getPotionManager().isVanilla()));
        }else if(args.length == 4 && args[0].equalsIgnoreCase("set")) {

            String profileName = args[1];

            if(!AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().containsKey(profileName)){
                player.sendMessage("§cInvalid profile! Case intensive?");
            }else{


                String key = args[2];
                String value = args[3];

                PotionProfileHook potionProfileHook = (PotionProfileHook) AspireSpigotHook.getInstance().getPotionHookManager().getProfile(profileName);

                if(key.equalsIgnoreCase("speed") && isDouble(value)){
                    potionProfileHook.setSpeed(Double.parseDouble(value));
                }else if(key.equalsIgnoreCase("canChangeIntensity") && isBoolean(value)){
                    potionProfileHook.setCanChangeIntensity(Boolean.parseBoolean(value));
                }else if(key.equalsIgnoreCase("minIntensityForFullHeal") && isDouble(value)){
                    potionProfileHook.setMinIntensityForFullHeal(Double.parseDouble(value));
                }else{
                    player.sendMessage("§cCant assign value to key.");
                    return;
                }
                player.sendMessage("§7Changed value from §5" + key + " §7to §5" + value + "§7!");
                potionProfileHook.applyChanges();
            }

        }else if(args.length == 2 && args[0].equalsIgnoreCase("check")) {

            String profileName = args[1];

            if(profileName.equalsIgnoreCase("server")){
                sendProfile(player, (PotionProfileHook) AspireSpigotHook.getInstance().getPotionHookManager().getFromSpigotProfile(AspireSpigot.getInstance().getPotionManager().getActiveProfile()));
            }else if(AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().containsKey(profileName)){
                sendProfile(player, (PotionProfileHook) AspireSpigotHook.getInstance().getPotionHookManager().getProfile(profileName));
            }else if(Bukkit.getPlayer(profileName) != null){
                sendProfile(player, (PotionProfileHook) AspireSpigotHook.getInstance().getPotionHookManager().getFromSpigotProfile(Bukkit.getPlayer(profileName).getPotionProfile()));
            }else{
                player.sendMessage("§cInvalid profile! Case intensive?");
            }

        }else if(args.length == 3 && args[0].equalsIgnoreCase("bind")){

            String profileName = args[2];
            if(!AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().containsKey(profileName)){
                player.sendMessage("§cInvalid profile! Case intensive?");
            }else{
                String playerName = args[1];
                if(playerName.equalsIgnoreCase("server")){

                    AspireSpigot.getInstance().getPotionManager().setActiveProfile((PotionProfile) AspireSpigotHook.getInstance().getPotionHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getPotionHookManager().getProfile(profileName)));
                    player.sendMessage("§7Server profile set to §d" + profileName +  "§7!");

                }else if(Bukkit.getPlayer(playerName) != null){
                    Player target = Bukkit.getPlayer(playerName);
                    target.setPotionProfile((PotionProfile) AspireSpigotHook.getInstance().getPotionHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getPotionHookManager().getProfile(profileName)));
                    player.sendMessage("§7PotionProfile of §d" + target.getName() + " §7set to §d" + profileName + "§7!");
                }else{
                    player.sendMessage("§cThere is no player with this name online!");
                }
            }
        }else{
            sendHelp(player);
        }

    }

    private void mainPotionGUI(Player player){
        InventoryGui gui = AspireSpigotHook.getInstance().gui(27).setName("§d§lPotion").build();
        gui.setItem(10, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.CHEST).setDisplayName("§6Profiles").build(), this::potionListGUI));
        gui.setItem(13, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.SIGN).setDisplayName("§5Info").setLore(Arrays.asList("§7Vanilla Pots: " + CCUtil.translateBoolean(AspireSpigot.getInstance().getPotionManager().isVanilla()),"§7Active: §b" + AspireSpigot.getInstance().getPotionManager().getActiveProfile().getName())).build(), (Consumer<Player>) null));
        gui.setItem(16, AspireSpigotHook.getInstance().guiItem(new ItemBuilder(Material.WORKBENCH).setDisplayName("§6Create").setLore(Collections.singletonList("§7Create a profile")).build(), this::createProfile));
        gui.open(player);
        gui.setDestroyOnClose(true);
    }

    private void potionListGUI(final Player player){

        InventoryGui gui = AspireSpigotHook.getInstance().gui(54).setName("§d§lPotion List").build();

        int i = 0;

        for(IProfileHook iProfileHook : AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().values()){
            if(!(iProfileHook instanceof PotionProfileHook)) continue;
            gui.setItem(i, AspireSpigotHook.getInstance().guiItem(AspireSpigotHook.getInstance().item(Material.POTION).setDisplayName("§d" + iProfileHook.getName()).build(), player1 -> sendProfile(player1, (PotionProfileHook)iProfileHook)));
            i++;
        }

        gui.open(player);
        gui.setDestroyOnClose(true);

    }

    private void createProfile(final Player player){

    }

    private void sendProfile(final Player receiver, final PotionProfileHook potionProfileHook){

        receiver.sendMessage(Messages.getHeader("§d§l","Potions"));
        receiver.sendMessage(" ");
        receiver.sendMessage(" §7Name: §b" + potionProfileHook.getName());
        receiver.sendMessage(" §7Speed: §b" + potionProfileHook.getSpeed());
        receiver.sendMessage(" §7Change Intensitity: §b" + (potionProfileHook.isCanChangeIntensity() ? potionProfileHook.getMinIntensityForFullHeal() : CCUtil.translateBoolean(false)));
        receiver.sendMessage(" ");
        receiver.sendMessage(Messages.getFooter("Potions"));
    }

    private void sendHelp(final Player player){
        player.sendMessage(Messages.getHeader("§d§l","Potions"));
        player.sendMessage(" ");
        player.sendMessage(" §7/pots §bmenu §8- §7open a simple gui.");
        player.sendMessage(" §7/pots §btoggle §8- §7toggle pots.");
        player.sendMessage(" §7/pots §bset <profile> <key> <value> §8- §7edit a profile");
        player.sendMessage(" §7/pots §bcheck <server,profile,player> §8- §7check a profile");
        player.sendMessage(" §7/pots §bind <server,player> <profile> §8- §7bind a profile");
        player.sendMessage(" ");
        player.sendMessage(Messages.getFooter("Potions"));
    }

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
            List<String> list = Arrays.asList("menu", "toggle", "set", "check", "bind");
            return list.stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }else if (args.length == 2 && args[0].equalsIgnoreCase("check")){
            List<String> list = new ArrayList<>();
            list.add("server");
            AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).forEachOrdered(list::add);
            Bukkit.getOnlinePlayers().stream().filter(all -> all.getName().toLowerCase().startsWith(chars.toLowerCase())).forEachOrdered(all -> list.add(all.getName()));
            return list.stream().filter(s -> s.toLowerCase().startsWith(chars)).collect(Collectors.toList());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("bind")){
            List<String> list = new ArrayList<>();
            list.add("server");
            Bukkit.getOnlinePlayers().stream().filter(all -> all.getName().toLowerCase().startsWith(chars.toLowerCase())).forEachOrdered(all -> list.add(all.getName()));
            return list.stream().filter(s -> s.toLowerCase().startsWith(chars)).collect(Collectors.toList());
        }else if(args.length == 3 && args[0].equalsIgnoreCase("bind")){
            return AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("set")){
            return AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().keySet().stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }else if(args.length == 2 && args[0].equalsIgnoreCase("set")){
            List<String> list = Arrays.asList("speed", "canChangeIntensity", "minIntensityForFullHeal");
            return list.stream().filter(s -> s.toLowerCase().startsWith(chars.toLowerCase())).collect(Collectors.toList());
        }
        else{
            return null;
        }
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

}
