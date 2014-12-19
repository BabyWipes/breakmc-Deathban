package com.breakmc.DeathBan.zeus;

import org.bukkit.command.*;
import java.lang.reflect.*;
import java.util.*;
import com.breakmc.DeathBan.zeus.registers.bukkit.*;

public class BukkitZeusSubCommand
{
    String parent;
    String name;
    String[] aliases;
    String permission;
    Object instance;
    
    public BukkitZeusSubCommand(final String parent, final String name, final String[] aliases, final String permission, final Object instance) {
        super();
        this.name = name;
        this.parent = parent;
        this.aliases = aliases;
        this.permission = permission;
        this.instance = instance;
    }
    
    public void execute(final CommandSender sender, final String[] args) {
        try {
            final Method method = BukkitRegistrar.getRawRegisteredSubcommands().get(this.parent).get(this.name);
            method.invoke(this.instance, sender, args);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public String getParent() {
        return this.parent;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String[] getAliases() {
        return this.aliases;
    }
}
