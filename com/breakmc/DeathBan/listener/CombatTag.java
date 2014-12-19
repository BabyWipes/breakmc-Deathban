package com.breakmc.DeathBan.listener;

import org.bukkit.inventory.*;
import org.bukkit.scheduler.*;
import com.breakmc.DeathBan.objects.*;
import com.breakmc.DeathBan.utils.*;
import com.breakmc.DeathBan.*;
import org.bukkit.plugin.*;
import org.bukkit.scoreboard.*;
import org.bukkit.event.*;
import org.bukkit.potion.*;
import org.bukkit.*;
import org.bukkit.event.entity.*;
import java.util.*;
import org.bukkit.entity.*;
import org.bukkit.event.player.*;

public class CombatTag implements Listener
{
    public static HashMap<Villager, ItemStack[]> items;
    private HashMap<Villager, String> logged;
    private HashMap<String, Villager> loggedVillager;
    private ArrayList<String> killed;
    private HashMap<String, Integer> inCombat;
    private HashMap<String, BukkitRunnable> inCombatTag;
    private ArrayList<String> stillAlive;
    private ArrayList<String> hasLogged;
    
    public CombatTag() {
        super();
        this.logged = new HashMap<Villager, String>();
        this.loggedVillager = new HashMap<String, Villager>();
        this.killed = new ArrayList<String>();
        this.inCombat = new HashMap<String, Integer>();
        this.inCombatTag = new HashMap<String, BukkitRunnable>();
        this.stillAlive = new ArrayList<String>();
        this.hasLogged = new ArrayList<String>();
    }
    
    @EventHandler
    public void onDamage(final EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            final Player damaged = (Player)e.getEntity();
            final Player damager = (Player)e.getDamager();
            final User user = UserManager.getInstance().getUserCache().get(damaged.getName());
            final User user2 = UserManager.getInstance().getUserCache().get(damager.getName());
            if (!this.inCombat.containsKey(damaged.getName())) {
                damaged.sendMessage(ChatColor.RED + "You are now in combat!");
                final Scoreboard scoreboard = damaged.getScoreboard();
                final Objective objective = (scoreboard.getObjective("gold") == null) ? scoreboard.registerNewObjective("gold", "dummy") : scoreboard.getObjective("gold");
                final Score combatTag = objective.getScore("§aCombat Tag");
                combatTag.setScore(60);
                this.inCombatTag.put(damaged.getName(), new BukkitRunnable() {
                    public void run() {
                        if (CombatTag.this.inCombat.get(damaged.getName()) != null) {
                            if (CombatTag.this.inCombat.get(damaged.getName()) > 0) {
                                CombatTag.this.inCombat.put(damaged.getName(), CombatTag.this.inCombat.get(damaged.getName()) - 1);
                                combatTag.setScore((int)CombatTag.this.inCombat.get(damaged.getName()));
                            }
                            else {
                                CombatTag.this.inCombat.remove(damaged.getName());
                                damaged.sendMessage(ChatColor.GREEN + "You are no longer in combat.");
                                scoreboard.resetScores("§aCombat Tag");
                                this.cancel();
                                CombatTag.this.inCombatTag.remove(damager.getName());
                            }
                        }
                    }
                });
                this.inCombatTag.get(damaged.getName()).runTaskTimerAsynchronously((Plugin)Main.getPlugin((Class)Main.class), 20L, 20L);
            }
            if (!this.inCombat.containsKey(damager.getName())) {
                damager.sendMessage(ChatColor.RED + "You are now in combat!");
                final Scoreboard scoreboard2 = damager.getScoreboard();
                final Objective objective2 = (scoreboard2.getObjective("gold") == null) ? scoreboard2.registerNewObjective("gold", "dummy") : scoreboard2.getObjective("gold");
                final Score combatTag2 = objective2.getScore("§aCombat Tag");
                combatTag2.setScore(60);
                this.inCombatTag.put(damager.getName(), new BukkitRunnable() {
                    public void run() {
                        if (CombatTag.this.inCombat.get(damager.getName()) != null) {
                            if (CombatTag.this.inCombat.get(damager.getName()) > 0) {
                                CombatTag.this.inCombat.put(damager.getName(), CombatTag.this.inCombat.get(damager.getName()) - 1);
                                combatTag2.setScore((int)CombatTag.this.inCombat.get(damager.getName()));
                            }
                            else {
                                CombatTag.this.inCombat.remove(damager.getName());
                                damager.sendMessage(ChatColor.GREEN + "You are no longer in combat.");
                                scoreboard2.resetScores("§aCombat Tag");
                                this.cancel();
                                CombatTag.this.inCombatTag.remove(damager.getName());
                            }
                        }
                    }
                });
                this.inCombatTag.get(damager.getName()).runTaskTimerAsynchronously((Plugin)Main.getPlugin((Class)Main.class), 20L, 20L);
            }
            this.inCombat.put(damaged.getName(), 60);
            this.inCombat.put(damager.getName(), 60);
        }
    }
    
    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        if (this.inCombat.containsKey(e.getEntity().getName()) && e.getEntity().isDead()) {
            final Player p = e.getEntity();
        }
    }
    
    @EventHandler
    public void onQuit(final PlayerQuitEvent e) {
        final Player p = e.getPlayer();
        final World w = p.getWorld();
        final Location loc = p.getLocation();
        if (this.inCombat.containsKey(p.getName())) {
            final Villager villager = (Villager)w.spawnEntity(p.getLocation(), EntityType.VILLAGER);
            villager.setAdult();
            villager.setHealth(20.0);
            final String prefix = ChatColor.GRAY + "[" + ChatColor.RED + "Logger" + ChatColor.GRAY + "] " + ChatColor.RESET;
            villager.setCustomName(prefix + p.getName());
            villager.setCustomNameVisible(true);
            villager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 10));
            final ItemStack[] armor = new ItemStack[4];
            final ItemStack[] contents = new ItemStack[36];
            for (int i = 0; i < p.getInventory().getSize(); ++i) {
                contents[i] = p.getInventory().getContents()[i];
            }
            for (int i = 0; i < 4; ++i) {
                armor[i] = p.getInventory().getArmorContents()[i];
            }
            final ItemStack[] fullInv = this.concat(armor, contents);
            CombatTag.items.put(villager, fullInv);
            this.logged.put(villager, p.getName());
            this.loggedVillager.put(p.getName(), villager);
            new BukkitRunnable() {
                public void run() {
                    if (villager.isValid()) {
                        CombatTag.items.remove(villager);
                        villager.remove();
                    }
                }
            }.runTaskLater((Plugin)Main.getPlugin((Class)Main.class), this.inCombat.get(p.getName()) * 20L);
            Bukkit.broadcastMessage(ChatColor.GOLD + "Combat Logger: " + ChatColor.GREEN + p.getName());
        }
    }
    
    @EventHandler
    public void onVillagerDeath(final EntityDeathEvent e) {
        final Entity ent = (Entity)e.getEntity();
        if (ent instanceof Villager) {
            final Villager villager = (Villager)ent;
            if (CombatTag.items.containsKey(villager)) {
                final ItemStack[] inv = CombatTag.items.get(villager);
                e.getDrops().addAll(Arrays.asList(inv));
                this.killed.add(this.logged.get(villager));
                CombatTag.items.remove(villager);
            }
        }
    }
    
    ItemStack[] concat(final ItemStack[] A, final ItemStack[] B) {
        final int aLen = A.length;
        final int bLen = B.length;
        final ItemStack[] C = new ItemStack[aLen + bLen];
        System.arraycopy(A, 0, C, 0, aLen);
        System.arraycopy(B, 0, C, aLen, bLen);
        return C;
    }
    
    @EventHandler
    public void onInteract(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager) {
            final Villager v = (Villager)e.getRightClicked();
            if (CombatTag.items.containsKey(v)) {
                e.setCancelled(true);
            }
        }
    }
    
    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        if (this.killed.contains(e.getPlayer().getName())) {
            e.getPlayer().getInventory().clear();
            e.getPlayer().setHealth(0.0);
            e.getPlayer().sendMessage("You have been killed while you were offline!");
            this.killed.remove(e.getPlayer().getName());
        }
        else {
            if (!this.loggedVillager.containsKey(e.getPlayer().getName())) {
                return;
            }
            if (this.loggedVillager == null) {
                return;
            }
            this.loggedVillager.get(e.getPlayer().getName()).remove();
        }
    }
    
    static {
        CombatTag.items = new HashMap<Villager, ItemStack[]>();
    }
}
