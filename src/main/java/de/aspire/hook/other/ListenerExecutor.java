package de.aspire.hook.other;

import de.aspire.hook.manager.event.EventManager;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.EventExecutor;

public class ListenerExecutor implements EventExecutor {


    private final Class<? extends Event> cls;
    private final EventManager.EventListener listener;

    private boolean disable = false;

    public ListenerExecutor(Class<? extends Event> cls, EventManager.EventListener listener) {
        this.cls = cls;
        this.listener = listener;
    }

    @Override
    public void execute(Listener listener, Event event) {
        if (this.disable) {
            event.getHandlers().unregister(listener);
            return;
        }

        if (this.cls.equals(event.getClass()))
            this.listener.on(event);
    }

    public EventManager.EventListener getListener() {
        return listener;
    }

    public void setDisable(boolean disable) {
        this.disable = disable;
    }
}
