package de.aspire.hook.manager.hook.impl;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IProfileHook;
import de.aspire.hook.manager.hook.HookManager;
import de.aspire.hook.profile.pots.PotionProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.api.IProfile;
import de.aspire.spigot.profiles.potion.PotionProfile;
import org.apache.commons.lang.IllegalClassException;

public class PotionHookManager extends HookManager{

    public PotionHookManager(){

        /* Check out the KnockbackHookManager for more information */

        for(IProfile iProfile : AspireSpigot.getInstance().getPotionManager().getProfileMap().values()){
            if(!(iProfile instanceof PotionProfile)) continue;
            PotionProfile potionProfile = (PotionProfile) iProfile;
            getProfileList().put(potionProfile.getName(), new PotionProfileHook(potionProfile.getName()));
        }
    }

    @Override
    public void update(IProfileHook iProfile) {
        if(iProfile == null) return;
        if(!(iProfile instanceof PotionProfileHook)) throw new IllegalClassException("IProfile cannot be casted to PotionProfile.class!");

        if(!AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().containsKey(iProfile.getName())) throw new NullPointerException("PotionProfile not found!");
        ((PotionProfileHook) iProfile).update();

    }

    @Override
    public void updateAll() {
        for(IProfileHook iProfile : AspireSpigotHook.getInstance().getPotionHookManager().getProfileList().values())
            update(iProfile);
    }

    @Override
    public IProfile getFromHookProfile(IProfileHook iProfileHook) {
        if(iProfileHook == null) return AspireSpigot.getInstance().getPotionManager().getActiveProfile();
        return AspireSpigot.getInstance().getPotionManager().getProfileMap().getOrDefault(iProfileHook.getName(),AspireSpigot.getInstance().getPotionManager().getActiveProfile());
    }

    @Override
    public void apply(IProfileHook iProfileHook) {
        iProfileHook.applyChanges();
    }

    @Override
    public void applyAll() {
        for(IProfileHook iProfileHook : getProfileList().values())
            apply(iProfileHook);
    }

    @Override
    public IProfile getSpigotDefault() {
        return AspireSpigot.getInstance().getPotionManager().getActiveProfile();
    }

    @Override
    public IProfileHook getHookDefault() {
        return getFromSpigotProfile(AspireSpigot.getInstance().getPotionManager().getActiveProfile());
    }

    @Override
    public void setDefault(IProfileHook iProfileHook) {
        AspireSpigot.getInstance().getPotionManager().setActiveProfile((PotionProfile) getFromHookProfile(iProfileHook));
    }

    @Override
    public void setDefault(String name) {
        if(!isValidProfile(name)) throw new NullPointerException("Potion Profile with name " + name + " not found!");
        AspireSpigot.getInstance().getPotionManager().setActiveProfile((PotionProfile) getFromHookProfile(getProfile(name)));
    }
}
