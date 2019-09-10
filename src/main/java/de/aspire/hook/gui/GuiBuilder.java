package de.aspire.hook.gui;

import de.aspire.hook.AspireSpigotHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.function.Consumer;

public class GuiBuilder {

    private final AspireSpigotHook plugin;

    private final GuiItem[] items;

    private String name;
    private Consumer<Player> closeEvent;

    private Inventory inventory;
    private boolean destroyOnClose;

    public GuiBuilder(int size, AspireSpigotHook plugin) {
        this.plugin = plugin;
        this.items = new GuiItem[size];
    }

    public GuiBuilder(Inventory inventory, AspireSpigotHook plugin) {
        this.plugin = plugin;
        this.inventory = inventory;
        this.items = new GuiItem[inventory.getSize()];
    }
    public GuiBuilder(InventoryType inventoryType, AspireSpigotHook plugin) {
        this.plugin = plugin;
        this.inventory = Bukkit.createInventory(null,inventoryType);
        this.items = new GuiItem[inventory.getSize()];
    }

    public GuiBuilder addItem(int slot, GuiItem guiItem) {
        this.items[slot] = guiItem;
        return this;
    }

    public GuiItem getItem(int slot) {
        return this.items[slot];
    }

    public GuiBuilder setName(String name) {
        this.name = (name.length() > 32 ? "TOO LONG" : ChatColor.translateAlternateColorCodes('&', name));
        return this;
    }
    public GuiBuilder setName(String name, boolean nameSplit) {
        if(!nameSplit)
            this.name = (name.length() > 32 ? "TOO LONG" : ChatColor.translateAlternateColorCodes('&', name));
        else
            this.name = (name.length() > 32 ? ChatColor.translateAlternateColorCodes('&',name.replace(name.substring(name.length() - (name.length() - 32 + 2)),"")) + ".." : ChatColor.translateAlternateColorCodes('&',name));
        return this;
    }

    public GuiBuilder onClose(Consumer<Player> closeEvent) {
        this.closeEvent = closeEvent;
        return this;
    }

    public GuiBuilder destroyOnClose() {
        this.destroyOnClose = true;
        return this;
    }

    public int getSize() {
        return this.items.length;
    }

    public InventoryGui build() {
        if (this.name == null)
            this.name = "";
        InventoryGui gui = new InventoryGui(plugin, items, name, inventory, closeEvent);
        if (destroyOnClose)
            gui.destroy();
        return gui;
    }

}
