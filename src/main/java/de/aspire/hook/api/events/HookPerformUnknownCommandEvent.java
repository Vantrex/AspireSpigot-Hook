package de.aspire.hook.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class HookPerformUnknownCommandEvent extends Event{

    private static HandlerList handlerList = new HandlerList();

    private String message;
    private final String command;

    public HookPerformUnknownCommandEvent(String message, String command){
        this.message = message;
        this.command = command;
    }


    public String getMessage() {
        return message;
    }

    public String getCommand() {
        return command;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}
