package com.breakmc.DeathBan.teams;

import java.util.regex.*;
import org.bukkit.scheduler.*;
import redis.clients.jedis.*;
import com.breakmc.DeathBan.*;
import org.apache.commons.lang.*;
import org.json.simple.parser.*;
import org.json.simple.*;
import java.io.*;
import org.bukkit.*;
import java.text.*;
import org.bukkit.plugin.*;
import org.bukkit.event.player.*;
import org.bukkit.event.*;
import org.bukkit.entity.*;
import java.util.*;

public class TeamManager implements Listener
{
    HashMap<String, Team> inTeam;
    ArrayList<Team> teams;
    ArrayList<String> teamChat;
    File inTeamFile;
    Pattern isJson;
    HashMap<String, BukkitRunnable> dontMove;
    HashMap<String, Long> onCooldown;
    long interval;
    Jedis db;
    protected static TeamManager instance;
    
    public TeamManager() {
        super();
        this.inTeam = new HashMap<String, Team>();
        this.teams = new ArrayList<Team>();
        this.teamChat = new ArrayList<String>();
        this.inTeamFile = new File(((Main)Main.getPlugin((Class)Main.class)).getDataFolder(), "inteam.json");
        this.isJson = Pattern.compile("([a-zA-Z0-9_]\\.json)");
        this.dontMove = new HashMap<String, BukkitRunnable>();
        this.onCooldown = new HashMap<String, Long>();
        this.interval = 600000L;
        this.db = ((Main)Main.getPlugin((Class)Main.class)).getJedis();
    }
    
    public static TeamManager getInstance() {
        return TeamManager.instance;
    }
    
    public Team createTeam(final Player player, final String name, final String password) {
        if (this.doesTeamExist(name)) {
            player.sendMessage("§cThe team '" + name + "' already exists!");
            return null;
        }
        if (this.isOnTeam(player.getName())) {
            player.sendMessage("§cYou are already on a team!");
            return null;
        }
        final Team team1 = new Team(name, player.getName(), new ArrayList<String>(), new ArrayList<String>(), password);
        player.sendMessage("§6You have created team '§f" + name + "§6'.");
        this.inTeam.put(player.getName(), team1);
        this.teams.add(team1);
        return team1;
    }
    
    public Team createTeam(final Player player, final String name) {
        return this.createTeam(player, name, "");
    }
    
    public boolean joinTeam(final String name, final String password, final Player player) {
        final Team team = this.matchTeam(name);
        if (this.inTeam.get(player.getName()) != null) {
            player.sendMessage("§cYou are already in a team!");
            return false;
        }
        if (team == null) {
            player.sendMessage("§cCould not find team '" + name + "'.");
            return false;
        }
        if (team.getPassword().isEmpty()) {
            this.messageTeam(team, "&6" + player.getName() + " &ehas joined the team!");
            player.sendMessage("§eYou have successfully joined the team!");
            team.getMembers().add(player.getName());
            this.inTeam.put(player.getName(), team);
            this.saveInTeam();
            this.saveTeam(team);
            return true;
        }
        if (password.isEmpty()) {
            player.sendMessage("§cTeam requires a password!");
            return false;
        }
        if (password.equals(team.getPassword())) {
            this.messageTeam(team, "&6" + player.getName() + " &ehas joined the team!");
            player.sendMessage("§eYou have successfully joined the team!");
            team.getMembers().add(player.getName());
            this.inTeam.put(player.getName(), team);
            this.saveInTeam();
            this.saveTeam(team);
            return true;
        }
        player.sendMessage("§cWrong password!");
        return false;
    }
    
    public void messageTeam(final Team team, final String message) {
        for (final String member : team.getMembers()) {
            final Player p = Bukkit.getPlayerExact(member);
            if (p == null) {
                continue;
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        for (final String member : team.getManagers()) {
            final Player p = Bukkit.getPlayerExact(member);
            if (p == null) {
                continue;
            }
            p.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
        final Player player = Bukkit.getPlayerExact(team.getLeader());
        if (player == null) {
            return;
        }
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    public Team getPlayerTeam(final Player player) {
        Validate.notNull((Object)player, "Player cannot be null!");
        return this.inTeam.get(player.getName());
    }
    
    public Team getPlayerTeam(final String player) {
        Validate.notNull((Object)player, "Player cannot be null!");
        if (this.inTeam.get(player) == null) {
            return null;
        }
        return this.inTeam.get(player);
    }
    
    public void sendInfo(final Player player, final String name) {
        final Team team = this.getPlayerTeam(name);
        if (team == null) {
            player.sendMessage("§cPlayer '" + name + "' is not on a team.");
            return;
        }
        if (this.getPlayerTeam(player) != null && this.getPlayerTeam(player).equals(team)) {
            this.sendInfo(player);
            return;
        }
        player.sendMessage("§e§m------§r §6" + team.getName() + " §e§m------");
        final StringBuilder builder = new StringBuilder();
        builder.append("§6§l" + team.getLeader());
        for (final String man : team.getManagers()) {
            builder.append("§e,").append("§6" + man);
        }
        for (final String mem : team.getMembers()) {
            builder.append("§e,").append("§e" + mem);
        }
        final String members = builder.toString().trim();
        player.sendMessage("§6§lLeader§e, §6Manager§e, Member");
        player.sendMessage("§eMembers: " + members);
    }
    
    public void sendInfo(final Player player, final Team team) {
        if (team == null) {
            player.sendMessage("§cTeam does not exist!");
        }
        if (this.getPlayerTeam(player).equals(team)) {
            this.sendInfo(player);
            return;
        }
        player.sendMessage("§e§m------§r §6" + team.getName() + " §e§m------");
        final StringBuilder builder = new StringBuilder();
        builder.append("§6§l" + team.getLeader());
        for (final String man : team.getManagers()) {
            builder.append("§e, ").append("§6" + man);
        }
        for (final String mem : team.getMembers()) {
            builder.append("§e, ").append("§e" + mem);
        }
        final String members = builder.toString().trim();
        player.sendMessage("§6§lLeader§e, §6Manager§e, Member");
        player.sendMessage("§eMembers: " + members);
    }
    
    public void sendInfo(final Player player) {
        if (this.inTeam.get(player.getName()) == null) {
            player.sendMessage("§cYou are not on any team!");
            return;
        }
        final Team team = this.inTeam.get(player.getName());
        player.sendMessage("§e§m------§r §6" + team.getName() + " §e§m------");
        player.sendMessage("§6Password: " + ((team.getPassword().isEmpty() || team.getPassword() == null) ? "§cNot Set" : ("§e" + team.getPassword())));
        player.sendMessage("§6Home: " + ((team.getHome() == null) ? "§cNot Set" : ("§aSet§e(" + this.formatLocation(team.getHome()) + ")")));
        player.sendMessage("§6Friendly Fire is " + (team.isFriendlyFire() ? "§cON" : "§aOFF"));
        final StringBuilder builder = new StringBuilder();
        builder.append("§6§l" + team.getLeader());
        for (final String man : team.getManagers()) {
            builder.append("§e,").append("§6" + man);
        }
        for (final String mem : team.getMembers()) {
            builder.append("§e,").append("§e" + mem);
        }
        final String members = builder.toString().trim();
        player.sendMessage("§6§lLeader§e, §6Manager§e, Member");
        player.sendMessage("§eMembers: " + members);
    }
    
    public boolean doesTeamExist(final String name) {
        for (final Team team : this.teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    
    public Team matchTeam(final String name) {
        for (final Team team : this.teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }
    
    public void loadTeams() {
        final File[] teamsFiles = ((Main)Main.getPlugin((Class)Main.class)).getTeamsFolder().listFiles();
        if (teamsFiles.length == 0 || !((Main)Main.getPlugin((Class)Main.class)).getTeamsFolder().exists()) {
            return;
        }
        for (final File file : teamsFiles) {
            this.loadTeam(file);
        }
    }
    
    public void loadInTeam() {
        if (!this.inTeamFile.exists()) {
            return;
        }
        final JSONParser parser = new JSONParser();
        try {
            final JSONObject object = (JSONObject)parser.parse(new FileReader(this.inTeamFile));
            final JSONArray array = object.get("inTeam");
            for (final Object obj : array) {
                final JSONObject jsonObject = (JSONObject)obj;
                final String name = jsonObject.get("name");
                final Team team = this.matchTeam(jsonObject.get("team"));
                this.inTeam.put(name, team);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void saveInTeam() {
        final JSONObject object = new JSONObject();
        final JSONArray array = new JSONArray();
        for (final Map.Entry<String, Team> entry : this.inTeam.entrySet()) {
            final JSONObject object2 = new JSONObject();
            object2.put("name", entry.getKey());
            object2.put("team", entry.getValue().getName());
            array.add(object2);
        }
        object.put("inTeam", array);
        try {
            final FileWriter writer = new FileWriter(this.inTeamFile);
            object.writeJSONString(writer);
            writer.flush();
            writer.close();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void loadTeam(final File file) {
        if (!file.getName().endsWith(".json")) {
            throw new UnsupportedOperationException("Unsupported file extension!");
        }
        try {
            final FileReader fileReader = new FileReader(file);
            final JSONParser parser = new JSONParser();
            final JSONObject object = (JSONObject)parser.parse(fileReader);
            final String name = object.get("name");
            String password;
            if (object.get("password") != null) {
                password = object.get("password");
            }
            else {
                password = "";
            }
            final boolean friendlyFire = object.get("friendlyFire");
            final String leader = object.get("leader");
            final JSONArray members = object.get("members");
            final ArrayList<String> memberss = new ArrayList<String>();
            for (final Object obj : members) {
                final String pnames = (String)obj;
                memberss.add(pnames);
            }
            final JSONArray managerss = object.get("managers");
            final ArrayList<String> managers = new ArrayList<String>();
            for (final Object obj2 : managerss) {
                managers.add((String)obj2);
            }
            Location home;
            if (object.get("home") != null) {
                home = this.locFromString(object.get("home"), ',');
            }
            else {
                home = null;
            }
            final Team team = new Team(name, leader, managers, memberss, password);
            team.setFriendlyFire(friendlyFire);
            if (home != null) {
                team.setHome(home);
            }
            this.teams.add(team);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void saveTeam(final Team team) {
        final File file = new File(((Main)Main.getPlugin((Class)Main.class)).getTeamsFolder(), team.getName() + ".json");
        if (file.exists()) {
            try {
                final JSONObject object = new JSONObject();
                final JSONArray members = new JSONArray();
                final JSONArray managers = new JSONArray();
                object.put("name", team.getName());
                if (!team.getPassword().isEmpty()) {
                    object.put("password", team.getPassword());
                }
                object.put("friendlyFire", team.isFriendlyFire());
                object.put("leader", team.getLeader());
                if (team.getMembers().isEmpty()) {
                    object.put("members", members);
                }
                else {
                    for (final String member : team.getMembers()) {
                        members.add(member);
                    }
                    object.put("members", members);
                }
                if (team.getManagers().isEmpty()) {
                    object.put("managers", managers);
                }
                else {
                    for (final String manager : team.getManagers()) {
                        managers.add(manager);
                    }
                    object.put("managers", managers);
                }
                if (team.getHome() != null) {
                    object.put("home", this.locToString(team.getHome(), ','));
                }
                final FileWriter fw = new FileWriter(file);
                object.writeJSONString(fw);
                fw.flush();
                fw.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            try {
                file.createNewFile();
                final JSONObject object = new JSONObject();
                final JSONArray members = new JSONArray();
                final JSONArray managers = new JSONArray();
                object.put("name", team.getName());
                if (!team.getPassword().isEmpty()) {
                    object.put("password", team.getPassword());
                }
                object.put("friendlyFire", team.isFriendlyFire());
                object.put("leader", team.getLeader());
                if (team.getMembers().isEmpty()) {
                    object.put("members", members);
                }
                else {
                    for (final String member : team.getMembers()) {
                        members.add(member);
                    }
                    object.put("members", members);
                }
                if (team.getManagers().isEmpty()) {
                    object.put("managers", managers);
                }
                else {
                    for (final String manager : team.getManagers()) {
                        managers.add(manager);
                    }
                    object.put("managers", managers);
                }
                if (team.getHome() != null) {
                    object.put("home", this.locToString(team.getHome(), ','));
                }
                final FileWriter writer = new FileWriter(file);
                object.writeJSONString(writer);
                writer.flush();
                writer.close();
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    
    public void leaveTeam(final Player player) {
        if (this.inTeam.get(player.getName()) == null) {
            player.sendMessage("§cYou are not in a team!");
        }
        else {
            final Team team = this.inTeam.get(player.getName());
            if (team.getLeader().equals(player.getName())) {
                player.sendMessage("§cYou cannot leave your team! Do /team disband!");
            }
            else {
                team.getMembers().remove(player.getName());
                team.getManagers().remove(player.getName());
                this.messageTeam(team, "§6" + player.getName() + " §ehas left the team!");
                player.sendMessage("§eYou have left the team!");
                this.saveTeam(team);
                this.inTeam.remove(player.getName());
                this.saveInTeam();
                this.teamChat.remove(player.getName());
                getInstance().updateTeam(team, TeamAction.UPDATE);
            }
        }
    }
    
    public String locToString(final Location loc, final char delim) {
        Validate.notNull((Object)loc, "Location cannot be null!");
        final StringBuilder builder = new StringBuilder();
        builder.append(loc.getWorld().getName()).append(delim).append(loc.getX()).append(delim).append(loc.getY()).append(delim).append(loc.getZ()).append(delim).append(loc.getYaw()).append(delim).append(loc.getPitch());
        return builder.toString();
    }
    
    public Location locFromString(final String serialized, final char delim) {
        Validate.notNull((Object)serialized, "Serialized location string cannot be null!");
        Validate.notNull((Object)delim, "Character delimeter cannot be null!");
        final String[] splitedLoc = serialized.split("" + delim);
        final World world = Bukkit.getWorld(splitedLoc[0]);
        final double x = Double.valueOf(splitedLoc[1]);
        final double y = Double.valueOf(splitedLoc[2]);
        final double z = Double.valueOf(splitedLoc[3]);
        final float yaw = Float.valueOf(splitedLoc[4]);
        final float pitch = Float.valueOf(splitedLoc[5]);
        return new Location(world, x, y, z, yaw, pitch);
    }
    
    public void saveTeams() {
        for (final Team team : this.teams) {
            this.saveTeam(team);
        }
    }
    
    public boolean isLeader(final Player player) {
        return this.getPlayerTeam(player) != null && this.getPlayerTeam(player).getLeader().equals(player.getName());
    }
    
    public boolean isManager(final Player player) {
        return this.getPlayerTeam(player) != null && this.getPlayerTeam(player).getManagers().contains(player.getName());
    }
    
    public String formatLocation(final Location location) {
        final DecimalFormat format = new DecimalFormat("#.##");
        return "X: " + format.format(location.getX()) + ", Y: " + format.format(location.getY()) + ", Z: " + format.format(location.getZ());
    }
    
    public boolean isOnTeam(final String player) {
        return this.inTeam.get(player) != null;
    }
    
    public File getInTeamFile() {
        return this.inTeamFile;
    }
    
    public HashMap<String, Team> getInTeam() {
        return this.inTeam;
    }
    
    public ArrayList<String> getTeamChat() {
        return this.teamChat;
    }
    
    public void disbandTeam(final Player p) {
        if (this.getPlayerTeam(p) == null) {
            p.sendMessage("§cYou are not a team!");
            return;
        }
        if (!this.isLeader(p)) {
            p.sendMessage("§cOnly the leader of the team may perform this command!");
            return;
        }
        final boolean deleted = new File(((Main)Main.getPlugin((Class)Main.class)).getTeamsFolder(), this.getPlayerTeam(p).getName() + ".json").delete();
        if (deleted) {
            p.sendMessage("§cYou have disbanded the team!");
            this.removePlayer(p.getName());
            for (final String member : this.getPlayerTeam(p).getMembers()) {
                this.inTeam.remove(member);
                if (Bukkit.getPlayer(member) != null) {
                    Bukkit.getPlayer(member).sendMessage("§cYour team has been disbanded!");
                }
                this.removePlayer(member);
            }
            for (final String manager : this.getPlayerTeam(p).getManagers()) {
                this.inTeam.remove(manager);
                if (Bukkit.getPlayer(manager) != null) {
                    Bukkit.getPlayer(manager).sendMessage("§cYour team has been disbanded!");
                }
                this.removePlayer(manager);
            }
            getInstance().updateTeam(this.getPlayerTeam(p), TeamAction.REMOVE);
            this.teams.remove(this.getPlayerTeam(p));
            this.inTeam.remove(p.getName());
            this.saveInTeam();
            this.saveTeams();
            System.gc();
        }
        else {
            System.out.println("An error occurred deleting the team.");
        }
    }
    
    public void teleportToHome(final Player p, final Location loc) {
        if (this.getPlayerTeam(p) == null) {
            p.sendMessage("§cYou are not on a team!");
            return;
        }
        if (loc == null) {
            p.sendMessage("§cTeam home is not set!");
            return;
        }
        if (this.onCooldown.containsKey(p.getName())) {
            final long now = System.currentTimeMillis();
            final long lastWarped = this.onCooldown.get(p.getName());
            final long earliestNext = lastWarped + this.interval;
            if (now < earliestNext) {
                final long timeRemaining = earliestNext - now;
                p.sendMessage("§cWarping on cooldown for another " + this.formatTime(timeRemaining));
                return;
            }
        }
        if (this.canTeleport(p)) {
            p.teleport(loc);
            p.sendMessage("§eWarped to your team's home!");
            this.onCooldown.put(p.getName(), System.currentTimeMillis());
        }
        else {
            this.dontMove.put(p.getName(), new BukkitRunnable() {
                public void run() {
                    p.teleport(loc);
                    TeamManager.this.onCooldown.put(p.getName(), System.currentTimeMillis());
                }
            });
            this.dontMove.get(p.getName()).runTaskLater((Plugin)Main.getPlugin((Class)Main.class), 200L);
            p.sendMessage("§cSomeone is nearby! Warping in 10 seconds! Do not move!");
        }
    }
    
    @EventHandler
    public void onMove(final PlayerMoveEvent e) {
        if (this.dontMove.containsKey(e.getPlayer().getName())) {
            this.dontMove.get(e.getPlayer().getName()).cancel();
            this.dontMove.remove(e.getPlayer().getName());
            e.getPlayer().sendMessage("§cYou moved! Warping cancelled!");
        }
    }
    
    public boolean canTeleport(final Player p) {
        for (final Entity ent : p.getNearbyEntities(40.0, 20.0, 40.0)) {
            if (ent instanceof Player) {
                final Player near = (Player)ent;
                if (near.equals(p)) {
                    continue;
                }
                if (this.getPlayerTeam(near) == null) {
                    return false;
                }
                if (this.getPlayerTeam(p).equals(this.getPlayerTeam(near))) {
                    continue;
                }
                return false;
            }
        }
        return true;
    }
    
    public String formatTime(final long time) {
        final long second = 1000L;
        final long minute = 60000L;
        final long minutes = time / minute;
        final long seconds = (time - minutes * minute) / second;
        if (seconds < 10L) {
            final String newSeconds = "0" + seconds;
            return minutes + "m" + newSeconds + "s";
        }
        return minutes + "m" + seconds + "s";
    }
    
    public void updatePlayer(final Player player, final Team team) {
        this.db.hset("team_players", player.getName(), team.getName());
    }
    
    public void updatePlayer(final String player, final Team team) {
        this.db.hset("team_players", player, team.getName());
    }
    
    public void removePlayer(final String player) {
        if (this.db.hexists("team_players", player)) {
            this.db.hdel("team_players", player);
        }
        else {
            System.out.println(player + " was not found in the database!");
        }
    }
    
    public void updateTeam(final Team team, final TeamAction action) {
        if (action.equals(TeamAction.UPDATE)) {
            final HashMap<String, String> hash = new HashMap<String, String>();
            this.db.hset("team_" + team.getName(), "leader", team.getLeader());
            final JSONArray managersArray = new JSONArray();
            if (!team.getManagers().isEmpty()) {
                for (final String managers : team.getManagers()) {
                    managersArray.add(managers);
                    this.db.hset("team_" + team.getName(), "managers", managers);
                }
                hash.put("managers", managersArray.toString());
            }
            else {
                hash.put("managers", "");
            }
            final JSONArray membersArray = new JSONArray();
            if (!team.getMembers().isEmpty()) {
                for (final String members : team.getMembers()) {
                    membersArray.add(members);
                    this.db.hset("team_" + team.getName(), "members", members);
                }
                hash.put("members", membersArray.toString());
            }
            else {
                hash.put("members", "");
            }
            System.out.println(membersArray.toString());
        }
        if (action.equals(TeamAction.REMOVE)) {
            if (this.db.exists("team_" + team.getName())) {
                this.db.hdel("team_" + team.getName(), "leader", "members", "managers");
            }
            else {
                System.out.println(team.getName() + " was not in the database!");
            }
        }
    }
    
    public List<String> matchPlayer(final String arg) {
        final List<String> strings = new ArrayList<String>();
        if (arg.isEmpty()) {
            strings.addAll(this.inTeam.keySet());
            return strings;
        }
        for (final Player names : Bukkit.getOnlinePlayers()) {
            if (names.getName().toLowerCase().equalsIgnoreCase(arg.toLowerCase())) {
                strings.clear();
                strings.add(names.getName());
                break;
            }
            if (names.getName().toLowerCase().startsWith(arg.toLowerCase())) {
                strings.add(names.getName());
            }
        }
        return strings;
    }
    
    public List<String> matchPlayerOnTeam(final String arg, final Team team) {
        final List<String> strings = new ArrayList<String>();
        if (arg.isEmpty()) {
            strings.addAll(this.inTeam.keySet());
            return strings;
        }
        for (final String managers : team.getManagers()) {
            if (managers.toLowerCase().equalsIgnoreCase(arg.toLowerCase())) {
                strings.clear();
                strings.add(managers);
                break;
            }
            if (!managers.toLowerCase().startsWith(arg.toLowerCase())) {
                continue;
            }
            strings.add(managers);
        }
        for (final String managers : team.getMembers()) {
            if (managers.toLowerCase().equalsIgnoreCase(arg.toLowerCase())) {
                strings.clear();
                strings.add(managers);
                break;
            }
            if (!managers.toLowerCase().startsWith(arg.toLowerCase())) {
                continue;
            }
            strings.add(managers);
        }
        return strings;
    }
    
    static {
        TeamManager.instance = new TeamManager();
    }
}
