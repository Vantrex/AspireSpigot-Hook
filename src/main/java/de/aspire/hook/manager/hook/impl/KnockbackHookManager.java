package de.aspire.hook.manager.hook.impl;


import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IProfileHook;
import de.aspire.hook.manager.hook.HookManager;
import de.aspire.hook.profile.knockback.KnockbackProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.api.IProfile;
import de.aspire.spigot.profiles.knockback.KnockbackProfile;
import org.apache.commons.lang.IllegalClassException;


public class KnockbackHookManager extends HookManager {



    public KnockbackHookManager(){

        /* Load all profiles */

        for(IProfile iProfile : AspireSpigot.getInstance().getKnockbackManager().getProfileMap().values()) {
            if (!(iProfile instanceof KnockbackProfile)) continue;
            KnockbackProfile knockbackProfile = (KnockbackProfile) iProfile;
            getProfileList().put(knockbackProfile.getName(), new KnockbackProfileHook(knockbackProfile.getName()));
        }
    }


    @Override
    public void update(IProfileHook iProfile) {
        if(iProfile == null) return;
        if(!(iProfile instanceof KnockbackProfileHook)) throw new IllegalClassException("IProfile cannot be casted to KnockbackProfileHook.class!");

        if(!AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().containsKey(iProfile.getName())) throw new NullPointerException("KnockbackProfile not found!");
        ((KnockbackProfileHook) iProfile).update();
    }




    @Override
    public void updateAll() {
        for(IProfileHook iProfile : AspireSpigotHook.getInstance().getKnockbackHookManager().getProfileList().values()){
            ((KnockbackProfileHook) iProfile).update();
        }
    }

    @Override
    public IProfile getFromHookProfile(IProfileHook iProfileHook) {
        if(iProfileHook == null) return AspireSpigot.getInstance().getKnockbackManager().getActiveProfile();
        return AspireSpigot.getInstance().getKnockbackManager().getProfileMap().getOrDefault(iProfileHook.getName(),AspireSpigot.getInstance().getKnockbackManager().getActiveProfile());
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
        return AspireSpigot.getInstance().getKnockbackManager().getActiveProfile();
    }

    @Override
    public IProfileHook getHookDefault() {
        return getFromSpigotProfile(AspireSpigot.getInstance().getKnockbackManager().getActiveProfile());
    }

    @Override
    public void setDefault(IProfileHook iProfileHook) {
        AspireSpigot.getInstance().getKnockbackManager().setActiveProfile((KnockbackProfile) getFromHookProfile(iProfileHook));
   }

    @Override
    public void setDefault(String name) {
       if(!isValidProfile(name)) throw new NullPointerException("Knockback Profile with name " + name + " not found!");
       AspireSpigot.getInstance().getKnockbackManager().setActiveProfile((KnockbackProfile) getFromHookProfile(getProfile(name)));
    }
}
