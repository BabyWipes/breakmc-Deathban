package com.breakmc.DeathBan.teams;

import com.breakmc.DeathBan.*;
import java.util.*;
import org.bukkit.command.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.zeus.annotations.*;

public class BaseTeamCommand
{
    private Main main;
    
    public BaseTeamCommand(final Main main) {
        super();
        this.main = main;
    }
    
    public TeamSubCommand getSubCommand(final String key) {
        TeamSubCommand tc = null;
        for (final TeamSubCommand sub : this.main.getTcommands()) {
            if (sub.getName().equalsIgnoreCase(key)) {
                tc = sub;
                return tc;
            }
            if (sub.getAliases().contains(key.toLowerCase())) {
                tc = sub;
                return tc;
            }
        }
        return tc;
    }
    
    @Command(name = "team", aliases = { "t", "deathbanistheshiz", "teamsisathing", "whenisteamspvpcomingback?" })
    public void handleTeam(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            final StringBuilder builder = new StringBuilder();
            for (final TeamSubCommand command : this.main.getTcommands()) {
                final String commandName = command.getName();
                builder.append(commandName).append(" | ");
            }
            String help = builder.toString().trim();
            if (help.endsWith("|")) {
                help = help.substring(0, help.length() - 1).trim();
            }
            sender.sendMessage("§c/team <" + help + ">");
            return;
        }
        try {
            final TeamSubCommand tc = this.getSubCommand(args[0]);
            tc.execute((Player)sender, this.fixArgs(args));
        }
        catch (Exception ex) {
            sender.sendMessage("§cAn error occured: " + ex.getMessage());
            ex.printStackTrace();
        }
    }
    
    public String[] fixArgs(final String[] args) {
        final String[] subArgs = new String[args.length - 1];
        for (int i = 1; i < args.length; ++i) {
            subArgs[i - 1] = args[i];
        }
        return subArgs;
    }
}
