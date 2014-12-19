package com.breakmc.DeathBan.teams.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.teams.*;

public class Info extends TeamSubCommand
{
    public Info() {
        super("info", Arrays.asList("i"));
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length > 1) {
            p.sendMessage("§c/team info [Player]");
            return;
        }
        if (args.length == 0) {
            TeamManager.getInstance().sendInfo(p);
            return;
        }
        if (args.length == 1) {
            TeamManager.getInstance().sendInfo(p, args[0]);
        }
    }
}
