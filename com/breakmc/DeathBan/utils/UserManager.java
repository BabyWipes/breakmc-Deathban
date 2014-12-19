package com.breakmc.DeathBan.utils;

import redis.clients.jedis.*;
import java.util.*;
import com.breakmc.DeathBan.objects.*;
import com.breakmc.DeathBan.*;
import org.bukkit.entity.*;

public class UserManager
{
    Jedis jedis;
    static UserManager instance;
    HashMap<String, User> userCache;
    ConfigurationManager manager;
    
    private UserManager() {
        super();
        this.userCache = new HashMap<String, User>();
        this.manager = ConfigurationManager.getInstance();
        this.jedis = ((Main)Main.getPlugin((Class)Main.class)).getJedis();
    }
    
    public static UserManager getInstance() {
        return UserManager.instance;
    }
    
    public User loadUser(final Player p) {
        if (!this.userCache.containsKey(p.getName())) {
            User user;
            if (this.manager.getGoldConfig().get("goldnuggets." + p.getName()) == null) {
                user = new User(p, 50);
                this.saveUser(user);
            }
            else {
                user = new User(p, this.manager.getGoldConfig().getInt("goldnuggets." + p.getName()));
            }
            return user;
        }
        return this.userCache.get(p.getName());
    }
    
    public HashMap<String, User> getUserCache() {
        return this.userCache;
    }
    
    public void saveUser(final User user) {
        try {
            final ConfigurationManager configurationManager = ConfigurationManager.getInstance();
            configurationManager.getGoldConfig().set("goldnuggets." + user.getPlayer().getName(), (Object)user.getGold());
            configurationManager.saveGold();
            this.userCache.put(user.getPlayer().getName(), user);
            this.jedis.hset("players", user.getPlayer().getName(), String.valueOf(user.getGold()));
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    static {
        UserManager.instance = new UserManager();
    }
}
