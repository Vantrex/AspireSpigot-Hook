package de.aspire.hook.listener;

import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.api.events.HookPerformUnknownCommandEvent;
import de.aspire.spigot.events.PerformUnknownCommandEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PerformUnknownCommandListener implements Listener{

    @EventHandler
    public void onPerformUnknownCommand(final PerformUnknownCommandEvent event){
        final String command = event.getCommand();
        String message = AspireSpigotHook.getInstance().getHookConfig().getMessage("unknown-command");
        final HookPerformUnknownCommandEvent hookPerformUnknownCommandEvent = new HookPerformUnknownCommandEvent(message,command);
        Bukkit.getPluginManager().callEvent(hookPerformUnknownCommandEvent);
        event.setMessage(hookPerformUnknownCommandEvent.getMessage());
    }

}
