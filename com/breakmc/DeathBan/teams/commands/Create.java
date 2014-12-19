package com.breakmc.DeathBan.teams.commands;

import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Create extends TeamSubCommand
{
    public Create() {
        super("create");
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 2) {
            p.sendMessage("§c/team create <Name> [Password]");
            return;
        }
        if (args.length == 1) {
            final Team team = TeamManager.getInstance().createTeam(p, args[0]);
            if (team != null) {
                TeamManager.getInstance().saveTeam(team);
                TeamManager.getInstance().updatePlayer(p, team);
                TeamManager.getInstance().updateTeam(team, TeamAction.UPDATE);
            }
            return;
        }
        if (args.length == 2) {
            final Team team = TeamManager.getInstance().createTeam(p, args[0], args[1]);
            if (team != null) {
                TeamManager.getInstance().saveTeam(team);
                TeamManager.getInstance().updatePlayer(p, team);
                TeamManager.getInstance().updateTeam(team, TeamAction.UPDATE);
            }
        }
    }
}
