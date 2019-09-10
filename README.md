# **AspireSpigot-Hook**

**This Plugin "hooks" into our custom Spigot to change the knockback/pots/.. via command**


HookCommand register:
```
AspireSpigotHook.getInstance().getCommandManager().registerCommand(new ExampleCommand("example", Arrays.asList("exampleAlias1","exampleAlias2"),"example.permission","This is the example command"));
```

HookCommand usage:
```
public class ExampleCommand extends HookCommand{
..
...
..
}
```

HookPlayer usage:

```
 IHookPlayer hookPlayer = AspireSpigotHook.getInstance().getHookPlayerManager().getHookPlayer(player);
 ```

Get KnockbackProfile:
```
  IHookPlayer hookPlayer = AspireSpigotHook.getInstance().getHookPlayerManager().getHookPlayer(player);
  hookPlayer.getKnockbackProfile();
```

Set KnockbackProfile:
```
  IHookPlayer hookPlayer = AspireSpigotHook.getInstance().getHookPlayerManager().getHookPlayer(player);
  hookPlayer.setKnockbackProfile("exampleProfile");
```




