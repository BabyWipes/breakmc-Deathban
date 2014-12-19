package com.breakmc.DeathBan.objects;

import org.bukkit.*;
import com.breakmc.DeathBan.utils.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;

public class ReflectedBedPacket
{
    String VERSION;
    Class<?> bedPacketClass;
    Object packet;
    
    public ReflectedBedPacket(final Location loc, final Player p) {
        super();
        this.VERSION = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            final Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + this.VERSION + ".EntityHuman");
            this.bedPacketClass = Class.forName("net.minecraft.server." + this.VERSION + ".PacketPlayOutBed");
            final Constructor<?> packetConstructor = this.bedPacketClass.getConstructor(entityPlayerClass, Integer.TYPE, Integer.TYPE, Integer.TYPE);
            this.packet = packetConstructor.newInstance(ReflectionUtils.getHandle((Entity)p), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public Object getPacket() {
        return this.packet;
    }
}
