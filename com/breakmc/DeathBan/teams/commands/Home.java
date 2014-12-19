package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Home extends TeamSubCommand
{
    public Home() {
        super("home", Arrays.asList("h"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 0) {
            p.sendMessage("§c/team home");
            return;
        }
        TeamManager.getInstance().teleportToHome(p, TeamManager.getInstance().getPlayerTeam(p).getHome());
    }
}
