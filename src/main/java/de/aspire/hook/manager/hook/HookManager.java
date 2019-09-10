package de.aspire.hook.manager.hook;

import de.aspire.hook.api.IHookManager;
import de.aspire.hook.api.IProfileHook;
import de.aspire.spigot.AspireSpigot;
import de.aspire.spigot.api.IProfile;

import java.util.HashMap;
import java.util.Map;

public abstract class HookManager implements IHookManager{

    private final Map<String,IProfileHook> profileList;

    protected HookManager(){
        this.profileList = new HashMap<>();
    }

    public Map<String, IProfileHook> getProfileList() {
        return profileList;
    }

    @Override
    public void addProfile(String name, IProfileHook iProfileHook) {
        if(this.profileList.containsKey(name)) return;
        this.profileList.put(name,iProfileHook);
    }

    @Override
    public IProfileHook getFromSpigotProfile(IProfile iProfile) {
        return this.profileList.getOrDefault(iProfile.getName(),null);
    }



    @Override
    public IProfileHook getProfile(String name) {
        return this.profileList.getOrDefault(name,null);
    }

    @Override
    public boolean isValidProfile(String name) {
        return this.profileList.containsKey(name);
    }

}
