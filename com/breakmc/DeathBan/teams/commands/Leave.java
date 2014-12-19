package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Leave extends TeamSubCommand
{
    public Leave() {
        super("leave", Arrays.asList("l"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length > 0) {
            p.sendMessage("§c/team leave");
            return;
        }
        TeamManager.getInstance().leaveTeam(p);
    }
}
