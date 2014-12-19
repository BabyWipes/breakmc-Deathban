package com.breakmc.DeathBan.commands;

import java.util.*;
import org.bukkit.entity.*;
import com.breakmc.DeathBan.objects.*;
import com.breakmc.DeathBan.utils.*;
import org.bukkit.*;
import org.bukkit.command.*;

public class CommandViewGold extends SubCommand
{
    ConfigurationManager manager;
    
    public CommandViewGold() {
        super("view", "deathban.gold.view", Arrays.asList("vg", "v"));
        this.manager = ConfigurationManager.getInstance();
    }
    
    @Override
    public void execute(final Player p, final String[] args) {
        if (args.length == 1 || args.length > 2) {
            p.sendMessage(ChatColor.RED + "/gold view <Player>");
            return;
        }
        final Player tv = Bukkit.getPlayerExact(args[1]);
        if (tv != null) {
            final int gold = UserManager.getInstance().getUserCache().get(tv.getName()).getGold();
            p.sendMessage("§c" + tv.getName() + " has §6" + gold + " §cgold.");
            return;
        }
        final OfflinePlayer tvo = Bukkit.getOfflinePlayer(args[1]);
        if (this.manager.getGoldConfig().get("goldnuggets." + tvo.getName()) == null) {
            p.sendMessage(ChatColor.RED + "Could not find user '" + tvo.getName() + "'.");
            return;
        }
        final int gold2 = this.manager.getGoldConfig().getInt("goldnuggets." + tvo.getName());
        p.sendMessage("§c" + tvo.getName() + " has §6" + gold2 + " §cgold.");
        String.format("(%d/%d)", Bukkit.getOnlinePlayers().length, Bukkit.getMaxPlayers());
    }
    
    @Override
    public void consoleExecute(final CommandSender sender, final String[] args) {
    }
}
