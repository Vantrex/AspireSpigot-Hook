package de.aspire.hook.profile.pots;


import de.aspire.hook.profile.ProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.api.IProfile;
import de.aspire.spigot.profiles.potion.PotionProfile;
import org.bukkit.Bukkit;

import java.util.logging.Level;

public class PotionProfileHook extends ProfileHook {

    private double speed; // pot y speed
    private double minIntensityForFullHeal; // minIntensity for full heal (explained @canChangeIntensity)
    private boolean canChangeIntensity; // if intensity can be changed by the plugin if pot is not healing the player 100%

    private final PotionProfile potionProfile;

    public PotionProfileHook(String name) {

        /* Check out the KnockbackProfileHook class for more information */

        super(name);
        potionProfile = (PotionProfile) AspireSpigot.getInstance().getPotionManager().getProfileMap().get(name);
        update();
    }


    public void update(){
        setSpeed(potionProfile.getSpeed());
        setCanChangeIntensity(potionProfile.isCanChangeIntensity());
        setMinIntensityForFullHeal(potionProfile.getMinIntensityForFullHeal());
        Bukkit.getLogger().log(Level.FINE, "[AspireSpigotHook] PotionProfile " + getName() + " loaded!");
    }

    @Override
    public boolean isValid() {
        return AspireSpigot.getInstance().getPotionManager().getProfileMap().containsKey(getName()) && AspireSpigot.getInstance().getPotionManager().getProfileMap().get(getName()) != null;
    }

    @Override
    public IProfile getSpigotProfile() {
        return potionProfile;
    }

    @Override
    public void applyChanges() {
        potionProfile.setSpeed(getSpeed());
        potionProfile.setMinIntensityForFullHeal(getMinIntensityForFullHeal());
        potionProfile.setCanChangeIntensity(isCanChangeIntensity());
        potionProfile.save();
        Bukkit.getLogger().log(Level.FINE, "[AspireSpigotHook] Changes to PotionProfile " + getName() + " applied!");
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setCanChangeIntensity(boolean canChangeIntensity) {
        this.canChangeIntensity = canChangeIntensity;
    }

    public void setMinIntensityForFullHeal(double minIntensityForFullHeal) {
        this.minIntensityForFullHeal = minIntensityForFullHeal;
    }

    public double getMinIntensityForFullHeal() {
        return minIntensityForFullHeal;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean isCanChangeIntensity() {
        return canChangeIntensity;
    }
}
