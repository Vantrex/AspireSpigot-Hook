package de.aspire.hook;

import de.aspire.hook.gui.GuiBuilder;
import de.aspire.hook.gui.GuiItem;
import de.aspire.hook.listener.KnockbackListener;
import de.aspire.hook.listener.PerformUnknownCommandListener;
import de.aspire.hook.listener.PlayerJoinQuitListener;
import de.aspire.hook.manager.CommandManager;
import de.aspire.hook.manager.HookPlayerManager;
import de.aspire.hook.manager.event.EventManager;
import de.aspire.hook.manager.hook.impl.KnockbackHookManager;
import de.aspire.hook.manager.hook.impl.PotionHookManager;
import de.aspire.hook.other.ItemBuilder;
import de.aspire.hook.utils.HookPluginUtil;
import de.aspire.hook.utils.file.impl.HookConfig;
import de.aspire.spigot.AspireSpigot;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Map;
import java.util.function.Consumer;

public class AspireSpigotHook extends JavaPlugin{



    private static AspireSpigotHook instance;

    private CommandManager commandManager;

    public static  boolean HOOKED = false; // access is public static, because we need easy access to this boolean and it´s usage
    private HookConfig hookConfig;
    private HookPlayerManager hookPlayerManager;
    private KnockbackHookManager knockbackHookManager;
    private PotionHookManager potionHookManager;
    private EventManager eventManager;

    @Override
    public void onEnable() {
        instance = this;
        HookPluginUtil hookPluginUtil = new HookPluginUtil(this);
        HOOKED = canHook();
        if(!HOOKED){
            System.out.println("AspireSpigot not found, the plugin will unload!");
            hookPluginUtil.disable();
            return;
        }

        this.hookConfig = new HookConfig();
        this.commandManager = new CommandManager();
        this.hookPlayerManager = new HookPlayerManager();
        this.knockbackHookManager = new KnockbackHookManager();
        this.potionHookManager  = new PotionHookManager();
        this.eventManager = new EventManager(this);
        registerListener();
    }

    private boolean canHook(){
        return AspireSpigot.class != null; // We´re checking if the Server is running with the Spigot
    }

    @Override
    public void onDisable() {
       if(!HOOKED) return; // We´re just returning here, because we do not have to save something here since the plugin is not fully loaded at this point.
    }

    private void registerListener(){
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerJoinQuitListener(), this);
        pm.registerEvents(new KnockbackListener(), this);
        pm.registerEvents(new PerformUnknownCommandListener(), this);
    }


    public static AspireSpigotHook getInstance() {
        return instance;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public HookConfig getHookConfig() {
        return hookConfig;
    }

    public HookPlayerManager getHookPlayerManager() {
        return hookPlayerManager;
    }

    public KnockbackHookManager getKnockbackHookManager() {
        return knockbackHookManager;
    }

    public PotionHookManager getPotionHookManager() {
        return potionHookManager;
    }

    //<editor-fold defaultstate="collapsed" desc="unregisterEvent">
    public void unregisterEvent(Class<? extends Event> cls, EventManager.EventListener listener) {
        this.eventManager.unregisterEvent(cls, listener);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="registerEvent">
    public void registerEvent(Class<? extends Event> cls, EventManager.EventListener listener) {
        this.eventManager.registerEvent(cls, listener);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTask">
    public BukkitTask runTask(Runnable runnable) {
        return getServer().getScheduler().runTask(this, runnable);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTaskAsync">
    public BukkitTask runTaskAsync(Runnable runnable) {
        return getServer().getScheduler().runTaskAsynchronously(this, runnable);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTaskLater">
    public BukkitTask runTaskLater(long delay, Runnable runnable) {
        return getServer().getScheduler().runTaskLater(this, runnable, delay);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTaskLaterAsync">
    public BukkitTask runTaskLaterAsync(long delay, Runnable runnable) {
        return getServer().getScheduler().runTaskLaterAsynchronously(this, runnable, delay);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTaskTimer">
    public BukkitTask runTaskTimer(long delay, long repeat, Runnable runnable) {
        return getServer().getScheduler().runTaskTimer(this, runnable, delay, repeat);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="runTaskTimerAsync">
    public BukkitTask runTaskTimerAsync(long delay, long repeat, Runnable runnable) {
        return getServer().getScheduler().runTaskTimerAsynchronously(this, runnable, delay, repeat);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gui">
    public GuiBuilder gui(int size) {
        size = size <= 9 ? 9 : size <= 18 ? 18 : size <= 27 ? 27 : size <= 36 ? 36 : size <= 45 ? 45 : 54;
        return new GuiBuilder(size, this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="gui">
    public GuiBuilder gui(Inventory inventory) {
        return new GuiBuilder(inventory, this);
    }

    public GuiBuilder gui(InventoryType inventoryType) {
        return new GuiBuilder(inventoryType, this);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="guiItem">
    public GuiItem guiItem(ItemStack item, Consumer<Player> callback) {
        return new GuiItem(item, callback);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="guiItem">
    public GuiItem guiItem(ItemStack item, Map<ClickType, Consumer<Player>> consumerMap) {
        return new GuiItem(item, consumerMap);
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="item">
    public ItemBuilder item(Material material) {
        return new ItemBuilder(material);
    }
    //</editor-fold>

}
