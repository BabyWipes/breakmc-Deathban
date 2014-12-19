package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Join extends TeamSubCommand
{
    public Join() {
        super("join", Arrays.asList("j"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 0 || args.length > 2) {
            p.sendMessage("§c/team join <Team> [Password]");
            return;
        }
        if (args.length == 1) {
            final boolean success = TeamManager.getInstance().joinTeam(args[0], "", p);
            if (success) {
                TeamManager.getInstance().updatePlayer(p, TeamManager.getInstance().getPlayerTeam(p));
                TeamManager.getInstance().updateTeam(TeamManager.getInstance().getPlayerTeam(p), TeamAction.UPDATE);
            }
            return;
        }
        if (args.length == 2) {
            final boolean success = TeamManager.getInstance().joinTeam(args[0], args[1], p);
            if (success) {
                TeamManager.getInstance().updatePlayer(p, TeamManager.getInstance().getPlayerTeam(p));
                TeamManager.getInstance().updateTeam(TeamManager.getInstance().getPlayerTeam(p), TeamAction.UPDATE);
            }
        }
    }
}
