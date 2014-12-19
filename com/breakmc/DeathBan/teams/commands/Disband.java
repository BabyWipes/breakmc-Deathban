package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Disband extends TeamSubCommand
{
    public Disband() {
        super("disband", true, Arrays.asList("dis"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 0) {
            p.sendMessage("§c/team disband");
            return;
        }
        TeamManager.getInstance().disbandTeam(p);
    }
}
