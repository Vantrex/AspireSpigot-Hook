package de.aspire.hook.other;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import net.minecraft.server.v1_8_R3.NBTTagList;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilder {

    private ItemStack item;

    private List<String> lore;

    public ItemBuilder(Material mat){
        this.item = new ItemStack(mat);
    }

    public ItemBuilder(ItemStack item){
        this.item = item;
    }

    public ItemBuilder(Material mat, int amount){
        this.item  = new ItemStack(mat, amount);
    }

    public ItemBuilder(Material mat, int amount, short data){
        this.item = new ItemStack(mat, amount, data);
    }

    public ItemBuilder addEnchantment(Enchantment ench, int level){
        item.addUnsafeEnchantment(ench, level);
        return this;
    }


    public String getName(){
        if(getDisplayName() == null || (getDisplayName().equalsIgnoreCase(""))){
            return getMaterialName();
        }else{
            return getDisplayName();
        }
    }

    public String getDisplayName(){
        ItemMeta meta = item.getItemMeta();
        return meta.getDisplayName();
    }
    public ItemBuilder setType(Material material){
        item.setType(material);
        return this;
    }
    public List<String> getLore(){
        return getItemMeta().getLore();
    }
    public ItemBuilder removeLore(int line){
        List<String> lore = getLore();
        lore.remove(line);
        setLore(lore);
        return this;
    }
    public Material getMaterial(){
        return item.getType();
    }
    public String getMaterialName(){
        return getMaterial().toString().replace("_"," ");
    }

    public ItemBuilder setDisplayName(String name){
        if(name == null){
            return this;
        }
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setAmount(int amount){
        item.setAmount(amount);
        return this;
    }

    public int getAmount(){
        return item.getAmount();
    }

    public ItemMeta getItemMeta(){
        return item.getItemMeta();
    }
    public ItemBuilder setUnbreakable(boolean unbreakable){
        ItemMeta meta = item.getItemMeta();
        meta.spigot().setUnbreakable(unbreakable);
        item.setItemMeta(meta);
        return this;
    }

    public void createLore(){
        lore = new ArrayList<String>();
    }

    public boolean hasLore(){
        ItemMeta meta = item.getItemMeta();
        return meta.hasLore();
    }

    public ItemBuilder setLore(List<String> lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);
        item.setItemMeta(meta);
        return this;
    }
    public ItemBuilder clearLore(){
        ItemMeta meta = item.getItemMeta();
        if(meta == null) return this;
        List<String> list = getLore();
        if(list == null) return this;
        list.clear();
        setLore(list);
        return this;
    }


    public ItemBuilder addLore(List<String> lore){
        ItemMeta meta = item.getItemMeta();
        List<String> current = null;
        if(hasLore()) {
            current = meta.getLore();
        }else{
            current = new ArrayList<String>();
        }
        for(String s : lore) {
               current.add('&', s);
        }

        meta.setLore(current);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setLore(String[] lore){
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addLore(String[] lore){
        ItemMeta meta = item.getItemMeta();
        if(!(hasLore())){
            createLore();
        }
        meta.setLore(this.lore);
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setDurability(short durability){
        item.setDurability(durability);
        return this;
    }
    public ItemBuilder setDurability(int durability){
        item.setDurability((short) durability);
        return this;
    }

    public ItemBuilder setItemMeta(ItemMeta meta){
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder addGlow(){
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        NBTTagList ench = new NBTTagList();
        tag.set("ench", ench);
        nmsStack.setTag(tag);
        item = CraftItemStack.asCraftMirror(nmsStack);
        return this;
    }
    public boolean hasGlow(){
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
        NBTTagCompound tag = null;
        if (!nmsStack.hasTag()) {
            tag = new NBTTagCompound();
            nmsStack.setTag(tag);
        }
        if (tag == null) tag = nmsStack.getTag();
        return tag.hasKey("ench");

    }

    public ItemBuilder addBookEnchantment(Enchantment enchantment, int level){
        EnchantmentStorageMeta meta = (EnchantmentStorageMeta) item.getItemMeta();
        meta.addStoredEnchant(enchantment, level, true);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setSkullOwner(String owner){
        SkullMeta meta = (SkullMeta)item.getItemMeta();
        meta.setOwner(Bukkit.getOfflinePlayer(owner).getName());
        item.setItemMeta(meta);
        return this;
    }

    public ItemStack build(){
        return item;
    }

    public ItemBuilder getSkullBySkinURL(String skinURL) {
        item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (skinURL.isEmpty()) return this;
        ItemMeta headMeta = item.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:[{Value:\"%s\"}]}", skinURL).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        item.setItemMeta(headMeta);
        return this;
    }

}