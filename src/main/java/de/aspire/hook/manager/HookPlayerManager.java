package de.aspire.hook.manager;

import de.aspire.hook.other.HookPlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HookPlayerManager{

    private final Map<UUID,HookPlayer> hookPlayers;

    public HookPlayerManager(){
        this.hookPlayers = new HashMap<>();
    }

    private void addToCache(UUID uuid, HookPlayer hookPlayer){
        if(this.hookPlayers.containsKey(uuid)) return;
        this.hookPlayers.put(uuid,hookPlayer);
    }

    public void removeFromCache(UUID uuid){
        if(!this.hookPlayers.containsKey(uuid)) return;
        this.hookPlayers.remove(uuid);
    }

    public HookPlayer getHookPlayer(UUID uuid){
        if(!this.hookPlayers.containsKey(uuid)) addToCache(uuid, new HookPlayer(uuid));
        return this.hookPlayers.get(uuid);
    }

    public HookPlayer getHookPlayer(Player player){
        return getHookPlayer(player.getUniqueId());
    }

}
