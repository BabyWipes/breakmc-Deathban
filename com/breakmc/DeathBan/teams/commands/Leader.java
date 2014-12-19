package com.breakmc.DeathBan.teams.commands;

import org.bukkit.entity.*;
import mkremins.fanciful.*;
import org.bukkit.*;
import com.breakmc.DeathBan.teams.*;
import java.util.*;

public class Leader extends TeamSubCommand
{
    public Leader() {
        super("leader", true);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 1) {
            p.sendMessage("/team leader <Player>");
            return;
        }
        if (!TeamManager.getInstance().isOnTeam(p.getName())) {
            p.sendMessage("§cYou are not on a team.");
            return;
        }
        final Team team = TeamManager.getInstance().getPlayerTeam(p);
        if (!TeamManager.getInstance().isLeader(p)) {
            p.sendMessage("§cYou must be a leader to perform  this command!");
        }
        else {
            if (args[0].equalsIgnoreCase(p.getName())) {
                p.sendMessage("§cYou may not set yourself to leader!");
                return;
            }
            final List<String> opts = TeamManager.getInstance().matchPlayerOnTeam(args[0], team);
            if (opts.size() <= 0) {
                p.sendMessage("§cCould not find player by the name of '" + args[0] + "'.");
                return;
            }
            if (opts.size() > 1) {
                final FancyMessage message = new FancyMessage("Did you mean: ").color(ChatColor.GOLD).then();
                for (int i = 0; i < opts.size(); ++i) {
                    if (i < opts.size() - 1) {
                        message.text(opts.get(i)).color(ChatColor.GRAY).tooltip("§eClick here to set '§6" + opts.get(i) + " §e' as team leader.").command("/team leader " + opts.get(i)).then(", ").color(ChatColor.YELLOW).then();
                    }
                    else {
                        message.text(opts.get(i)).color(ChatColor.GRAY).tooltip("§eClick here to set '6" + opts.get(i) + " §e' as team leader.").command("/team leader " + opts.get(i));
                    }
                }
                message.send(p);
                return;
            }
            final String adnaskasdvbajkdbsjk = opts.get(0);
            if (TeamManager.getInstance().isOnTeam(adnaskasdvbajkdbsjk)) {
                if (TeamManager.getInstance().getPlayerTeam(adnaskasdvbajkdbsjk).equals(team)) {
                    team.setLeader(adnaskasdvbajkdbsjk);
                    team.getManagers().add(p.getName());
                    TeamManager.getInstance().messageTeam(team, String.format("§ePlayer '§6%s' §eis now team leader!", adnaskasdvbajkdbsjk));
                }
            }
            else {
                p.sendMessage("§cPlayer '" + adnaskasdvbajkdbsjk + "' is not on any team!");
            }
        }
    }
}
