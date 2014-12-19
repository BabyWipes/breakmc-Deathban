package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Chat extends TeamSubCommand
{
    public Chat() {
        super("chat", Arrays.asList("c", "ch"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (TeamManager.getInstance().isOnTeam(p.getName())) {
            if (TeamManager.getInstance().getTeamChat().contains(p.getName())) {
                p.sendMessage("§eTeam Chat: §cOFF");
                TeamManager.getInstance().getTeamChat().remove(p.getName());
            }
            else {
                p.sendMessage("§eTeam Chat: §aON");
                TeamManager.getInstance().getTeamChat().add(p.getName());
            }
        }
        else {
            p.sendMessage("§cYou are not on a team!");
        }
    }
}
