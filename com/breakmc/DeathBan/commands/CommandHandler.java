package com.breakmc.DeathBan.commands;

import com.breakmc.DeathBan.*;
import java.util.*;
import org.bukkit.command.*;
import com.breakmc.DeathBan.utils.*;
import com.breakmc.DeathBan.objects.*;
import org.bukkit.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.zeus.annotations.*;

public class CommandHandler
{
    private Main main;
    
    public CommandHandler(final Main main) {
        super();
        this.main = main;
    }
    
    public SubCommand getSubCommand(final String key) {
        SubCommand sc = null;
        for (final SubCommand sub : this.main.getCommands()) {
            if (sub.getName().equalsIgnoreCase(key)) {
                sc = sub;
                return sc;
            }
            if (sub.getAliases().contains(key.toLowerCase())) {
                sc = sub;
                return sc;
            }
        }
        return sc;
    }
    
    @Command(name = "gold", aliases = { "g" }, usage = "§c/<command> <subcommand> [<args...>]", desc = "The base command for DeathBan. Contains multiple utilities.")
    public boolean handle(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            final int gold = (UserManager.getInstance().getUserCache().get(sender.getName()) == null) ? 0 : UserManager.getInstance().getUserCache().get(sender.getName()).getGold();
            sender.sendMessage(ChatColor.RED + "You have §6" + gold + "§c gold.");
            return true;
        }
        try {
            final SubCommand sc = this.getSubCommand(args[0]);
            if (!sc.getPermission().isEmpty() && !sender.hasPermission(sc.getPermission())) {
                sender.sendMessage("§cPermission Denied.");
                return true;
            }
            sc.execute((Player)sender, args);
            return true;
        }
        catch (Exception ex) {
            sender.sendMessage("§cAn error occured: " + ex.getMessage());
            return false;
        }
    }
}
