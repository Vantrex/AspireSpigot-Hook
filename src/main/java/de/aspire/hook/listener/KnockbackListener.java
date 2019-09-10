package de.aspire.hook.listener;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.spigot.events.profile.impl.knockback.KnockbackEvent;
import de.aspire.spigot.events.profile.impl.knockback.KnockbackUpdateEvent;
import de.aspire.spigot.events.profile.impl.knockback.PlayerKnockbackChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class KnockbackListener implements Listener{

    @EventHandler

    public void onKnockbackEvent(KnockbackEvent event){

        /* KnockbackUpdateEvent */
        if(event instanceof KnockbackUpdateEvent){
            /* Updating the profile */
            AspireSpigotHook.getInstance().getKnockbackHookManager().update( AspireSpigotHook.getInstance().getKnockbackHookManager().getFromSpigotProfile(event.getKnockbackProfile()));
        }

        /* PlayerKnockbackChangeEvent */
        if(event instanceof PlayerKnockbackChangeEvent){

        }

    }

}
