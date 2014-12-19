package com.breakmc.DeathBan.listener;

import org.bukkit.event.player.*;
import com.breakmc.DeathBan.utils.*;
import com.breakmc.DeathBan.objects.*;
import org.bukkit.event.*;

public class OnQuit implements Listener
{
    @EventHandler
    public void onQuit(final PlayerQuitEvent ev) {
        final User user = UserManager.getInstance().loadUser(ev.getPlayer());
        UserManager.getInstance().saveUser(user);
    }
}
