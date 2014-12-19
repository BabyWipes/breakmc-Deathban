package com.breakmc.DeathBan.commands;

import org.bukkit.command.*;
import com.breakmc.DeathBan.*;
import com.breakmc.DeathBan.zeus.annotations.*;
import com.breakmc.DeathBan.listener.*;

public class DBHandler
{
    @Command(name = "db", aliases = { "deathban" }, usage = "§c/<command> [<subcommand> | <args...>] [args...]", minArgs = 1, permission = "db.admin")
    public void handle(final CommandSender sender, final String[] args) {
        sender.sendMessage("§c/db [<subcommand> | <args...>] [args...]");
    }
    
    @SubCommand(parent = "db", name = "takelive", permission = "db.admin")
    public void takeLive(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§c/db takelive <Player>");
            return;
        }
        Main.getUtils().takeLive(args[0]);
        sender.sendMessage("§aSuccess");
    }
    
    @SubCommand(parent = "db", name = "takelives", permission = "db.admin")
    public void takeLives(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§c/db takelives <Player> <Amount>");
            return;
        }
        try {
            Main.getUtils().takeLives(args[0], Integer.parseInt(args[1]));
            sender.sendMessage("§aSuccess");
        }
        catch (Exception ex) {
            sender.sendMessage("§cAn error has occurred! Is the amount a integer?");
        }
    }
    
    @SubCommand(parent = "db", name = "addlive", permission = "db.admin")
    public void addLive(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§c/db addlive <Player>");
            return;
        }
        Main.getUtils().addLive(args[0]);
        sender.sendMessage("§aSuccess");
    }
    
    @SubCommand(parent = "db", name = "addlives", permission = "db.admin")
    public void addLives(final CommandSender sender, final String[] args) {
        if (args.length != 2) {
            sender.sendMessage("§c/db addlives <Player> <Amount>");
            return;
        }
        try {
            Main.getUtils().addLives(args[0], Integer.parseInt(args[1]));
            sender.sendMessage("§aSuccess");
        }
        catch (Exception ex) {
            sender.sendMessage("§cAn error has occurred! Is the amount a integer?");
        }
    }
    
    @SubCommand(parent = "db", name = "removeban", permission = "db.admin")
    public void removeBan(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§c/db removeban <Player>");
            return;
        }
        if (DeathKick.getDeathbanned().containsKey(args[0])) {
            DeathKick.getDeathbanned().remove(args[0]);
            sender.sendMessage("§cPlayer unbanned.");
        }
        else {
            sender.sendMessage("§cPlayer not found.");
        }
    }
    
    @SubCommand(parent = "db", name = "getlives", permission = "db.admin")
    public void getLives(final CommandSender sender, final String[] args) {
        if (args.length != 1) {
            sender.sendMessage("§c/db getlives <Player>");
            return;
        }
        final int amount = Main.getUtils().getPlayerLives(args[0]);
        sender.sendMessage("§6" + args[0] + "'s has §f" + amount + " §6" + ((amount != 1) ? "lives" : "life"));
    }
}
