package com.breakmc.DeathBan.commands;

import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.command.*;

public abstract class SubCommand
{
    String name;
    String permission;
    List<String> aliases;
    
    public SubCommand(final String name) {
        this(name, "", new LinkedList<String>());
    }
    
    public SubCommand(final String name, final String permission) {
        this(name, permission, new LinkedList<String>());
    }
    
    public SubCommand(final String name, final String permission, final List<String> aliases) {
        super();
        this.name = name;
        this.permission = permission;
        this.aliases = aliases;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getPermission() {
        return this.permission;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    public abstract void execute(final Player p0, final String[] p1);
    
    public abstract void consoleExecute(final CommandSender p0, final String[] p1);
}
