package com.breakmc.DeathBan.teams.listeners;

import org.bukkit.event.player.*;
import com.breakmc.DeathBan.teams.*;
import org.bukkit.event.*;

public class ChatListener implements Listener
{
    @EventHandler
    public void onChat(final AsyncPlayerChatEvent event) {
        if (TeamManager.getInstance().getTeamChat().contains(event.getPlayer().getName())) {
            event.setCancelled(true);
            String name = event.getPlayer().getName();
            if (TeamManager.getInstance().isLeader(event.getPlayer())) {
                name = "§6§l" + name;
            }
            else if (TeamManager.getInstance().isManager(event.getPlayer())) {
                name = "§6" + name;
            }
            else {
                name = "§e" + name;
            }
            TeamManager.getInstance().messageTeam(TeamManager.getInstance().getPlayerTeam(event.getPlayer()), String.format("§e[§6%s§e] %s§e: %s", TeamManager.getInstance().getPlayerTeam(event.getPlayer()).getName(), name, event.getMessage()));
        }
    }
}
