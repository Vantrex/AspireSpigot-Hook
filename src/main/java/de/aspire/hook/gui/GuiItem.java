package de.aspire.hook.gui;


import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.function.Consumer;

public class GuiItem {

    private Consumer<Player> consumer;

    private ItemStack itemStack;
    private InventoryGui inventory;
    private ClickType clickType;
    private Map<ClickType,Consumer<Player>> clickTypeConsumerMap;

    public GuiItem(ItemStack itemStack, Map<ClickType,Consumer<Player>> clickTypeConsumerMap) {
        this.consumer = null;
        this.clickTypeConsumerMap = clickTypeConsumerMap;
        this.itemStack = itemStack;
        this.clickType = clickType;
    }

    public void setClickTypeConsumerMap(Map<ClickType, Consumer<Player>> clickTypeConsumerMap) {
        this.clickTypeConsumerMap = clickTypeConsumerMap;
    }



    public GuiItem(ItemStack itemStack, Consumer<Player> consumer) {
        this.clickTypeConsumerMap = null;
        this.consumer = consumer;
        this.itemStack = itemStack;
        this.clickType = null;
    }

    //<editor-fold desc="compareItems">
    public boolean compareItems(ItemStack itemStack) {
        if (this.itemStack == null || itemStack == null || this.itemStack.getItemMeta() == null || itemStack.getItemMeta() == null || this.itemStack.getItemMeta().getDisplayName() == null || itemStack.getItemMeta().getDisplayName() == null)
            return false;
        if (this.itemStack.getItemMeta().getLore() != null && itemStack.getItemMeta().getLore() != null)
            if (!this.itemStack.getItemMeta().getLore().equals(itemStack.getItemMeta().getLore()))
                return false;
        if (this.itemStack.getAmount() != itemStack.getAmount())
            return false;
        return this.itemStack.getItemMeta().getDisplayName().equals(itemStack.getItemMeta().getDisplayName());
    }
    //</editor-fold>

    //<editor-fold desc="click">
    public void click(Player player) {
        if (consumer != null)
            consumer.accept(player);
    }

    public void setConsumer(Consumer<Player> consumer) {
        this.consumer = consumer;
    }

    public Consumer<Player> getConsumer() {
        return consumer;
    }

    public void click(Player player, ClickType clickType){
        this.clickTypeConsumerMap.get(clickType).accept(player);
    }

    public Map<ClickType, Consumer<Player>> getClickTypeConsumerMap(){
        return this.clickTypeConsumerMap;
    }

    public boolean clickMapContainsClickType(ClickType clickType){
        return this.clickTypeConsumerMap.containsKey(clickType);
    }

    //</editor-fold>

    //<editor-fold desc="getItemStack">
    public ItemStack getItemStack() {
        return itemStack;
    }
    //</editor-fold>


    //<editor-fold desc="setItemStack">
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
    //</editor-fold>

    //<editor-fold desc="setInventory">
    public void setInventory(InventoryGui inventory) {
        this.inventory = inventory;
    }
    //</editor-fold>

    //<editor-fold desc="update">
    public void update() {
        inventory.update(this);
    }
    //</editor-fold>
}
