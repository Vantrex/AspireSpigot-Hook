package de.aspire.hook.listener;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.IHookPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerJoinQuitListener implements Listener{

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event){
        AspireSpigotHook.getInstance().getHookPlayerManager().getHookPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent event){
        AspireSpigotHook.getInstance().getHookPlayerManager().removeFromCache(event.getPlayer().getUniqueId());
    }

}
