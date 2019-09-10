package de.aspire.hook.manager.event;



import de.aspire.hook.AspireSpigotHook;
import de.aspire.hook.other.ListenerExecutor;
import org.bukkit.event.Event;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager {


    private final AspireSpigotHook plugin;

    private final HashMap<EventListener, CopyOnWriteArrayList<ListenerExecutor>> executors;

    public EventManager(AspireSpigotHook plugin) {
        this.plugin = plugin;
        this.executors = new HashMap<>();
    }

    //<editor-fold desc="registerEvent">
    public void registerEvent(Class<? extends Event> cls, EventListener listener) {
        ListenerExecutor executor = new ListenerExecutor(cls, listener);

        this.plugin.getServer().getPluginManager().registerEvent(cls, new Listener() {
        }, EventPriority.NORMAL, executor, this.plugin);
        if (!this.executors.containsKey(listener))
            this.executors.put(listener, new CopyOnWriteArrayList<>());
        this.executors.get(listener).add(executor);
    }
    //</editor-fold>

    //<editor-fold desc="unregisterEvent">
    public void unregisterEvent(Class<? extends Event> cls, EventListener listener) {
        if (!this.executors.containsKey(listener))
            return;
        this.executors.get(listener).stream().filter((executor) -> (executor.getListener().equals(listener))).forEach((executor) ->
                executor.setDisable(true));
    }
    //</editor-fold>

    //<editor-fold desc="EventListener">
    public interface EventListener<T extends Event> {
        public void on(T event);
    }
    //</editor-fold>

}
