package com.breakmc.DeathBan.listener;

import org.bukkit.event.entity.*;
import java.util.*;
import com.breakmc.DeathBan.objects.*;
import com.breakmc.DeathBan.*;
import org.bukkit.plugin.*;
import org.bukkit.scheduler.*;
import com.gmail.filoghost.holograms.api.*;
import com.breakmc.DeathBan.utils.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.scoreboard.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;

public class EntityDeath implements Listener
{
    @EventHandler
    public void onEDeath(final EntityDeathEvent e) {
        final Entity p = (Entity)e.getEntity();
        if (e.getEntity().getKiller() != null) {
            if (MobGoldDropManager.getValueMap().get(e.getEntityType()) != null) {
                int amountToAdd = new Random().nextInt(MobGoldDropManager.getValueMap().get(e.getEntityType()));
                if (amountToAdd == 0) {
                    ++amountToAdd;
                }
                final User user = UserManager.getInstance().getUserCache().get(e.getEntity().getKiller().getName());
                user.setGold(user.getGold() + amountToAdd);
                try {
                    final Scoreboard scoreboard = e.getEntity().getKiller().getScoreboard();
                    final Objective objective = (scoreboard.getObjective("gold") == null) ? scoreboard.registerNewObjective("gold", "dummy") : scoreboard.getObjective("gold");
                    objective.setDisplayName("§c" + e.getEntity().getKiller().getName() + "'s Stats");
                    objective.setDisplaySlot(DisplaySlot.SIDEBAR);
                    final Score gold = objective.getScore("§6§lGold");
                    gold.setScore(user.getGold());
                    e.getEntity().getKiller().setScoreboard(scoreboard);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
                final Hologram hologram = HolographicDisplaysAPI.createHologram((Plugin)Main.getPlugin((Class)Main.class), p.getLocation().add(0.0, 1.4, 0.0), new String[] { "§6+" + amountToAdd + " gold" });
                new BukkitRunnable() {
                    public void run() {
                        hologram.delete();
                    }
                }.runTaskLater((Plugin)Main.getPlugin((Class)Main.class), 30L);
            }
            if (MobHealManager.getValueMap().get(e.getEntityType()) != null) {
                final int toHeal = MobHealManager.getValueMap().get(e.getEntityType());
                final Player killer = e.getEntity().getKiller();
                e.getEntity().getKiller().setHealth((e.getEntity().getKiller().getHealth() + toHeal > 20.0) ? 20.0 : (killer.getHealth() + toHeal));
            }
            else if (e.getEntity().getType() != EntityType.PLAYER) {
                e.getEntity().getKiller().setHealth((e.getEntity().getKiller().getHealth() + 1.0 > 20.0) ? 20.0 : (e.getEntity().getKiller().getHealth() + 1.0));
            }
            else {
                final Player killer2 = e.getEntity().getKiller();
                killer2.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 300, 1));
                killer2.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 300, 1));
                killer2.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 300, 4));
                killer2.playSound(killer2.getLocation(), Sound.WITHER_SPAWN, 2.0f, 2.0f);
                killer2.sendMessage("§c§lBLOODLUST ENGAGED!");
            }
        }
    }
}
