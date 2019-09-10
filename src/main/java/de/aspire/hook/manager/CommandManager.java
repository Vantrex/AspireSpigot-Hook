package de.aspire.hook.manager;

import de.aspire.hook.commands.HookCommand;
import de.aspire.hook.commands.impl.KnockbackCommand;
import de.aspire.hook.commands.impl.PotionCommand;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {

    private Map<String, HookCommand> commands;

    public CommandManager(){
        this.commands = new HashMap<>();
        registerAll();
    }

    public Map<String, HookCommand> getCommands() {
        return commands;
    }

    private void registerAll(){
        registerCommand(new KnockbackCommand("knockback", Collections.singletonList("kb"),"","KB Command"));
        registerCommand(new PotionCommand("potions",Arrays.asList("pots","potion"),"","Pot Command"));
    }

    public void registerCommand(HookCommand command) {
        commands.put(command.getName(), command);
        System.out.println("Command " + command.getName() + " registered.");
    }

    public void unregisterAll(){
        for(HookCommand hookCommand : commands.values())
            unregisterCommand(hookCommand);
    }

    private void unregisterCommand(HookCommand command) {
        HookCommand.unregisterCommand(command);
    }

}
