package de.aspire.hook.gui;



import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.manager.event.EventManager;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class InventoryGui {

    private final AspireSpigotHook plugin;

    private final GuiItem[] guiItems;
    private final String name;
    private final Consumer<Player> closeConsumer;

    private Inventory inventory;
    private boolean destroy, destroyOnClose;

    private EventManager.EventListener<InventoryClickEvent> clickEvent;
    private EventManager.EventListener<InventoryCloseEvent> closeEvent;

    public InventoryGui(AspireSpigotHook plugin, GuiItem[] guiItems, String name, Inventory inventory, Consumer<Player> closeConsumer) {
        this.plugin = plugin;
        this.guiItems = guiItems;
        this.name = name;
        this.closeConsumer = closeConsumer;

        createInventory(inventory);
        init();
    }

    public int getSize(){
        return this.inventory.getSize();
    }

    public String getName(){
        return this.inventory.getName();
    }

    public InventoryType getType(){
        return this.inventory.getType();
    }

    //<editor-fold desc="init">
    private void init() {

        //<editor-fold desc="InventoryClickEvent">
        clickEvent = (InventoryClickEvent event) -> {

            if (event.getClick().isShiftClick() && event.getInventory().equals(this.inventory)) {
                event.setCancelled(true);
                return;
            }
            if (event.getWhoClicked() == null || !event.getWhoClicked().getOpenInventory().getTopInventory().equals(this.inventory))
                return;
            event.setCancelled(true);

            if (event.getClickedInventory() == null || event.getWhoClicked() == null)
                return;

            if (event.getClickedInventory().equals(event.getWhoClicked().getInventory()))
                return;


            for (GuiItem item : this.guiItems) {
                if (item == null || !item.compareItems(event.getCurrentItem()))
                    continue;
                if(item.getClickTypeConsumerMap() == null && item.getConsumer() == null) continue;
                if(item.getClickTypeConsumerMap() == null)
                    item.click((Player) event.getWhoClicked());
                else if(item.clickMapContainsClickType(event.getClick()))
                    item.click((Player) event.getWhoClicked(),event.getClick());
                break;
            }
        };
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="InventoryCloseEvent">
        closeEvent = (InventoryCloseEvent event) -> {
            Player player = (Player) event.getPlayer();

            if (destroy || !event.getInventory().equals(this.inventory))
                return;
            plugin.runTaskAsync(() -> {
                if (closeConsumer != null)
                    closeConsumer.accept(player);
            });
            if (!destroyOnClose)
                return;
            destroy();
        };
        //</editor-fold>

        plugin.registerEvent(InventoryClickEvent.class, clickEvent);
        plugin.registerEvent(InventoryCloseEvent.class, closeEvent);
    }
    //</editor-fold>

    //<editor-fold desc="createInventory">
    private void createInventory(Inventory inventory) {
        this.inventory = (inventory == null ? plugin.getServer().createInventory(null, guiItems.length, name) : plugin.getServer().createInventory(null,inventory.getType(),name));



        for (int i = 0; i < guiItems.length; i++) {
            GuiItem item = guiItems[i];
            if (item == null)
                continue;
            this.inventory.setItem(i, item.getItemStack().clone());
            item.setInventory(this);
        }
    }
    //</editor-fold>

    //<editor-fold desc="setItem">
    public void setItem(int slot, GuiItem guiItem) {
        guiItems[slot] = guiItem;
        this.inventory.setItem(slot, guiItem.getItemStack().clone());
        guiItem.setInventory(this);
    }
    //</editor-fold>

    //<editor-fold desc="removeItem">
    public void removeItem(GuiItem guiItem) {
        for (int i = 0; i < this.guiItems.length; i++) {
            if (this.guiItems[i] != guiItem)
                continue;
            this.guiItems[i] = null;
            this.inventory.remove(guiItem.getItemStack());
            guiItem.setInventory(null);
        }
    }
    //</editor-fold>


    public Inventory getInventory() {
        return inventory;
    }

    //<editor-fold desc="destroy">
    public InventoryGui destroy() {
        destroy = true;
        plugin.unregisterEvent(InventoryClickEvent.class, clickEvent);
        plugin.unregisterEvent(InventoryCloseEvent.class, closeEvent);
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="update">
    public InventoryGui update(GuiItem guiItem) {
        for (int i = 0; i < this.guiItems.length; i++) {
            GuiItem item = this.guiItems[i];
            if (!guiItem.equals(item))
                continue;
            this.inventory.setItem(i, item.getItemStack().clone());
        }
        return this;
    }
    //</editor-fold>

    //<editor-fold desc="open">
    public InventoryGui open(Player player) {
        player.openInventory(this.inventory);
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isDestroyed">
    public boolean isDestroyed() {
        return destroy;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="setDestroyOnClose">
    public InventoryGui setDestroyOnClose(boolean destroyOnClose) {
        this.destroyOnClose = destroyOnClose;
        return this;
    }
    //</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="isDestroyOnClose">
    public boolean isDestroyOnClose() {
        return destroyOnClose;
    }
    //</editor-fold>

}
