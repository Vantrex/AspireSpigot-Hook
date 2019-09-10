package de.aspire.hook.other;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IHookPlayer;
import de.aspire.hook.profile.knockback.KnockbackProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.profiles.knockback.KnockbackProfile;
import de.aspire.spigot.profiles.potion.PotionProfile;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;


public class HookPlayer implements IHookPlayer{

    private UUID uuid;

    public HookPlayer(UUID uuid){
        this.uuid = uuid;
    }

    public Player getBukkitPlayer(){
        return Bukkit.getPlayer(uuid);
    }

    @Override
    public void setKnockbackProfile(String knockbackProfile) {
     getBukkitPlayer().setKnockbackProfile((KnockbackProfile) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getKnockbackHookManager().getProfile(knockbackProfile)));
    }


    @Override
    public void setKnockbackProfile(KnockbackProfileHook knockbackProfile) {
        getBukkitPlayer().setKnockbackProfile((KnockbackProfile) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromHookProfile(knockbackProfile));
    }

    @Override
    public KnockbackProfileHook getKnockbackProfile() {
        return (KnockbackProfileHook) AspireSpigotHook.getInstance().getKnockbackHookManager().getFromSpigotProfile(getBukkitPlayer().getKnockbackProfile());
    }


    @Override
    public void setPotionProfile(String potionProfile) {
        getBukkitPlayer().setPotionProfile((PotionProfile) AspireSpigotHook.getInstance().getPotionHookManager().getFromHookProfile(AspireSpigotHook.getInstance().getPotionHookManager().getProfile(potionProfile)));
    }

    @Override
    public String getPotionProfile() {
        return getBukkitPlayer().getPotionProfile().getName();
    }


}
