package com.breakmc.DeathBan.teams;

import java.util.*;
import org.bukkit.entity.*;

public abstract class TeamSubCommand
{
    String name;
    boolean manager;
    boolean leaderOnly;
    List<String> aliases;
    
    public TeamSubCommand(final String name) {
        this(name, false, new LinkedList<String>(), false);
    }
    
    public TeamSubCommand(final String name, final boolean manager) {
        this(name, manager, new LinkedList<String>(), false);
    }
    
    public TeamSubCommand(final String name, final List<String> aliases) {
        this(name, false, aliases, false);
    }
    
    public TeamSubCommand(final String name, final boolean manager, final List<String> aliases, final boolean leaderOnly) {
        super();
        this.name = name;
        this.manager = manager;
        this.aliases = aliases;
        this.leaderOnly = leaderOnly;
    }
    
    public TeamSubCommand(final String name, final boolean leaderOnly, final List<String> aliases) {
        super();
        this.name = name;
        this.leaderOnly = leaderOnly;
        this.manager = false;
        this.aliases = aliases;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isManager() {
        return this.manager;
    }
    
    public List<String> getAliases() {
        return this.aliases;
    }
    
    public abstract void execute(final Player p0, final String[] p1);
    
    public boolean isLeader() {
        return this.leaderOnly;
    }
    
    public boolean canPerformTeam() {
        return false;
    }
}
