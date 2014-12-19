package com.breakmc.DeathBan;

import org.bukkit.plugin.java.*;
import com.breakmc.DeathBan.zeus.registers.*;
import redis.clients.jedis.*;
import com.breakmc.DeathBan.zeus.registers.bukkit.*;
import org.bukkit.*;
import org.bukkit.event.*;
import com.breakmc.DeathBan.spawn.*;
import com.breakmc.DeathBan.teams.listeners.*;
import com.breakmc.DeathBan.listener.*;
import com.breakmc.DeathBan.teams.*;
import com.breakmc.DeathBan.objects.*;
import java.net.*;
import java.io.*;
import com.breakmc.DeathBan.utils.*;
import org.bukkit.plugin.*;
import org.bukkit.entity.*;
import java.nio.channels.*;
import com.breakmc.DeathBan.commands.*;
import com.breakmc.DeathBan.teams.commands.*;
import java.util.*;
import org.bukkit.command.*;
import com.breakmc.DeathBan.zeus.annotations.*;

public class Main extends JavaPlugin
{
    ConfigurationManager manager;
    public List<SubCommand> commands;
    public List<TeamSubCommand> tcommands;
    File teamsFolder;
    Registrar registrar;
    Jedis jedis;
    static DBUtils utils;
    
    public Main() {
        super();
        this.manager = ConfigurationManager.getInstance();
        this.commands = new LinkedList<SubCommand>();
        this.tcommands = new LinkedList<TeamSubCommand>();
        this.teamsFolder = new File(this.getDataFolder() + File.separator + "teams" + File.separator);
        this.jedis = new Jedis("localhost");
    }
    
    public void onEnable() {
        super.onEnable();
        this.manager.setup((Plugin)this);
        this.registrar = new BukkitRegistrar();
        this.setupSubCommands();
        this.setupTeamCommands();
        this.jedis.connect();
        this.jedis.auth("trAdRApAw2sP834e4ReJAYemAhetrEcr");
        Main.utils = new DBUtils(this);
        final PluginManager manager = Bukkit.getPluginManager();
        manager.registerEvents((Listener)new OnJoin(), (Plugin)this);
        manager.registerEvents((Listener)new BlockBreak(), (Plugin)this);
        manager.registerEvents((Listener)new OnQuit(), (Plugin)this);
        manager.registerEvents((Listener)new EntityDeath(), (Plugin)this);
        manager.registerEvents((Listener)new SpawnListener(), (Plugin)this);
        manager.registerEvents((Listener)new DeathKick(), (Plugin)this);
        manager.registerEvents((Listener)new Spawn(), (Plugin)this);
        manager.registerEvents((Listener)new FriendlyFireListener(), (Plugin)this);
        manager.registerEvents((Listener)new ChatListener(), (Plugin)this);
        manager.registerEvents((Listener)new ForDrugsYo(), (Plugin)this);
        manager.registerEvents((Listener)TeamManager.getInstance(), (Plugin)this);
        this.registrar.registerCommand("gold", new CommandHandler(this));
        this.registrar.registerCommand("team", new BaseTeamCommand(this));
        this.registrar.registerAll(new DBHandler());
        this.registrar.registerAllSubCommands(new DBHandler());
        this.registrar.registerAll(this);
        if (Bukkit.getOnlinePlayers().length > 0) {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                UserManager.getInstance().getUserCache().put(player.getName(), UserManager.getInstance().loadUser(player));
                try {
                    ScoreboardHandler.setupScoreboard(UserManager.getInstance().getUserCache().get(player.getName()));
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        if (!new File(this.getDataFolder(), "death_ban_gold_drops.txt").exists()) {
            try {
                final URL website = new URL("https://www.dropbox.com/s/n8l46zvbjwhvegl/death_ban_gold_drops.txt?dl=1");
                final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                final FileOutputStream fos = new FileOutputStream(new File(this.getDataFolder(), "death_ban_gold_drops.txt"));
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        if (!new File(this.getDataFolder(), "death-ban-health-points.txt").exists()) {
            try {
                final URL website = new URL("https://www.dropbox.com/s/2t2ptwos47ey793/death-ban-health-points.txt?dl=1");
                final ReadableByteChannel rbc = Channels.newChannel(website.openStream());
                final FileOutputStream fos = new FileOutputStream(new File(this.getDataFolder(), "death-ban-health-points.txt"));
                fos.getChannel().transferFrom(rbc, 0L, Long.MAX_VALUE);
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        try {
            final MobGoldDropManager mobGoldDropManager = new MobGoldDropManager(new File(this.getDataFolder(), "death_ban_gold_drops.txt"));
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final MobHealManager mobHealManager = new MobHealManager(new File(this.getDataFolder(), "death-ban-health-points.txt"));
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        if (!this.teamsFolder.exists()) {
            this.teamsFolder.mkdir();
        }
        if (!TeamManager.getInstance().getInTeamFile().exists()) {
            try {
                TeamManager.getInstance().getInTeamFile().createNewFile();
            }
            catch (Exception ex2) {
                ex2.printStackTrace();
            }
        }
        System.out.println("Loading teams into memory...");
        TeamManager.getInstance().loadTeams();
        System.out.println("Loading inTeams into memory...");
        TeamManager.getInstance().loadInTeam();
        System.out.println("Loading lives into memory");
        Main.utils.loadLives();
    }
    
    void setupSubCommands() {
        this.commands.add(new CommandViewGold());
    }
    
    void setupTeamCommands() {
        this.tcommands.add(new Create());
        this.tcommands.add(new Info());
        this.tcommands.add(new Join());
        this.tcommands.add(new Chat());
        this.tcommands.add(new Home());
        this.tcommands.add(new SetHome());
        this.tcommands.add(new SetPassword());
        this.tcommands.add(new Kick());
        this.tcommands.add(new Disband());
        this.tcommands.add(new SetFriendlyFire());
        this.tcommands.add(new Leave());
        this.tcommands.add(new Demote());
        this.tcommands.add(new Promote());
        this.tcommands.add(new Leader());
    }
    
    public void onDisable() {
        super.onDisable();
        for (final User user : UserManager.getInstance().getUserCache().values()) {
            UserManager.getInstance().saveUser(user);
            System.out.println("Saved User: " + user.getPlayer().getName());
        }
        Main.utils.saveLives();
        TeamManager.getInstance().saveInTeam();
        TeamManager.getInstance().saveTeams();
        this.jedis.disconnect();
    }
    
    @Command(name = "heal", aliases = { "h", "healmehbish" }, permission = "deathban.heal", maxArgs = 1, usage = "§c/<command> [player]", permissionMsg = "§4§lNO PERMS BITCH!")
    public void heal(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            final Player p = (Player)sender;
            p.setHealth(p.getMaxHealth());
        }
        else {
            if (args[0].equalsIgnoreCase("all")) {
                for (final Player p2 : Bukkit.getOnlinePlayers()) {
                    p2.setHealth(p2.getMaxHealth());
                    p2.sendMessage("§6You have been healed.");
                }
                sender.sendMessage("§6Healed all.");
                return;
            }
            final Player p3 = Bukkit.getPlayerExact(args[0]);
            if (p3 == null) {
                sender.sendMessage("§cCould not find user '" + args[0] + "'.");
                return;
            }
            p3.setHealth(p3.getMaxHealth());
            sender.sendMessage("§6Healed §f" + args[0]);
            p3.sendMessage("§6You have been healed.");
        }
    }
    
    @Command(name = "feed", aliases = { "f", "sate", "feedmehbish" }, permission = "deathban.feed", maxArgs = 1, usage = "§c/<command> [player]", permissionMsg = "§4§lNO PERMS BITCH!")
    public void feed(final CommandSender sender, final String[] args) {
        if (args.length == 0) {
            final Player p = (Player)sender;
            p.setSaturation(20.0f);
            p.setFoodLevel(20);
        }
        else {
            if (args[0].equalsIgnoreCase("all")) {
                for (final Player p2 : Bukkit.getOnlinePlayers()) {
                    p2.setSaturation(20.0f);
                    p2.setFoodLevel(20);
                    p2.sendMessage("§6You have been fed.");
                }
                sender.sendMessage("§6Fed all.");
                return;
            }
            final Player p3 = Bukkit.getPlayerExact(args[0]);
            if (p3 == null) {
                sender.sendMessage("§cCould not find user '" + args[0] + "'.");
                return;
            }
            p3.setFoodLevel(20);
            sender.sendMessage("§6Fed §f" + args[0]);
            p3.sendMessage("§6You have been fed.");
        }
    }
    
    public List<SubCommand> getCommands() {
        return this.commands;
    }
    
    public List<TeamSubCommand> getTcommands() {
        return this.tcommands;
    }
    
    public File getTeamsFolder() {
        return this.teamsFolder;
    }
    
    public Jedis getJedis() {
        return this.jedis;
    }
    
    public static DBUtils getUtils() {
        return Main.utils;
    }
}
