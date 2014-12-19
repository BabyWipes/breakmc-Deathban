package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class SetPassword extends TeamSubCommand
{
    public SetPassword() {
        super("pass", true, Arrays.asList("spw", "spwd", "setpw", "setpwd", "pass", "password", "setpassword"), false);
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length != 1) {
            p.sendMessage("§c/team pass <Password>");
            return;
        }
        if (!TeamManager.getInstance().isOnTeam(p.getName())) {
            p.sendMessage("§cYou are not on a team!");
            return;
        }
        if (!TeamManager.getInstance().isManager(p) && !TeamManager.getInstance().isLeader(p)) {
            p.sendMessage("§cYou must be at least a manager to perform this command!");
            return;
        }
        final Team team = TeamManager.getInstance().getPlayerTeam(p);
        if (args[0].equalsIgnoreCase("none") || args[0].equalsIgnoreCase("null") || args[0].equalsIgnoreCase("nil")) {
            team.setPassword("");
            p.sendMessage("§ePassword protection: §cOFF");
            TeamManager.getInstance().messageTeam(team, "§6" + p.getName() + " §ehas turned password protection §cOFF§e!");
            TeamManager.getInstance().saveTeam(team);
            return;
        }
        final String old_pass = team.getPassword();
        team.setPassword(args[0]);
        p.sendMessage("§ePassword set to '§6" + team.getPassword() + "§e'.");
        TeamManager.getInstance().messageTeam(team, "§6" + p.getName() + " §ehas changed the password from '§6" + old_pass + "§e' to '§6" + team.getPassword() + "§e'.");
        TeamManager.getInstance().saveTeam(team);
    }
}
