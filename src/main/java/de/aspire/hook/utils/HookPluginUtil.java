package de.aspire.hook.utils;

import de.aspire.hook.AspireSpigotHook;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.plugin.*;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HookPluginUtil {

    private final AspireSpigotHook aspireSpigotHook;

    /* PluginUtil I wrote back in the days with DevPlayerHD */

    public HookPluginUtil(final AspireSpigotHook aspireSpigotHook){
        this.aspireSpigotHook = aspireSpigotHook;
    }

    public void disable(){
        try {
            disablePlugin();
        } catch (InvalidPluginException | InvalidDescriptionException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    private void disablePlugin() throws UnknownDependencyException, InvalidPluginException, InvalidDescriptionException, NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException {

        PluginManager manager = this.aspireSpigotHook.getServer().getPluginManager();
        SimplePluginManager spmanager = (SimplePluginManager)manager;

        Plugin pluginunload = manager.getPlugin(this.aspireSpigotHook.getDescription().getName());
        manager.disablePlugin(pluginunload);

        if (spmanager != null) {
            Field pluginsField = spmanager.getClass().getDeclaredField("plugins");
            pluginsField.setAccessible(true);
            List plugins = (List)pluginsField.get(spmanager);

            Field lookupNamesField = spmanager.getClass().getDeclaredField("lookupNames");
            lookupNamesField.setAccessible(true);
            Map lookupNames = (Map)lookupNamesField.get(spmanager);

            Field commandMapField = spmanager.getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            SimpleCommandMap commandMap = (SimpleCommandMap)commandMapField.get(spmanager);

            Field knownCommandsField = null;
            Map knownCommands = null;

            if (commandMap != null) {
                knownCommandsField = commandMap.getClass().getDeclaredField("knownCommands");
                knownCommandsField.setAccessible(true);
                knownCommands = (Map)knownCommandsField.get(commandMap);
            }

            for (Plugin plugin : manager.getPlugins()) {
                if (plugin.getDescription().getName().equalsIgnoreCase(this.aspireSpigotHook.getDescription().getName())) {
                    manager.disablePlugin(plugin);

                    if ((plugins != null) && (plugins.contains(plugin))) {
                        plugins.remove(plugin);
                    }

                    if ((lookupNames != null) && (lookupNames.containsKey(plugin.getDescription().getName()))) {
                        lookupNames.remove(this.aspireSpigotHook.getDescription().getName());
                    }

                    if (commandMap != null) {
                        for (Iterator it = knownCommands.entrySet().iterator(); it.hasNext(); ) {
                            Map.Entry entry = (Map.Entry)it.next();

                            if ((entry.getValue() instanceof PluginCommand)) {
                                PluginCommand command = (PluginCommand)entry.getValue();

                                if (command.getPlugin() == plugin) {
                                    command.unregister(commandMap);
                                    it.remove();
                                }
                            }
                        }
                    }
                }
            }
        }

    }
}
