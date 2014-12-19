package com.breakmc.DeathBan.utils;

import org.bukkit.entity.*;
import java.lang.reflect.*;
import org.bukkit.*;

public class ParticleUtils
{
    private static String version;
    static Object packet;
    private static Method getHandle;
    private static Method sendPacket;
    private static Field playerConnection;
    private static Class<?> packetType;
    
    private static void setField(final String field, final Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field f = ParticleUtils.packet.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(ParticleUtils.packet, value);
    }
    
    public static void spawnParticles(final Location loc, final Player receivingPacket, final String packetname, final int amount) throws ClassNotFoundException, IllegalAccessException, InstantiationException, NoSuchMethodException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
        ParticleUtils.packet = ParticleUtils.packetType.newInstance();
        setField("a", packetname);
        setField("b", loc.getBlockX());
        setField("c", loc.getBlockY());
        setField("d", loc.getBlockZ());
        setField("e", 1);
        setField("f", 1);
        setField("g", 1);
        setField("h", 0);
        setField("i", amount);
        final Object player = ParticleUtils.getHandle.invoke(receivingPacket, new Object[0]);
        final Object connection = ParticleUtils.playerConnection.get(player);
        ParticleUtils.sendPacket.invoke(connection, ParticleUtils.packet);
    }
    
    private static String getCraftPlayerClasspath() {
        return "org.bukkit.craftbukkit." + ParticleUtils.version + ".entity.CraftPlayer";
    }
    
    private static String getPlayerConnectionClasspath() {
        return "net.minecraft.server." + ParticleUtils.version + ".PlayerConnection";
    }
    
    private static String getNMSPlayerClasspath() {
        return "net.minecraft.server." + ParticleUtils.version + ".EntityPlayer";
    }
    
    private static String getPacketClasspath() {
        return "net.minecraft.server." + ParticleUtils.version + ".Packet";
    }
    
    private static String getPacketPlayOutParticles() {
        if (Integer.valueOf(ParticleUtils.version.split("_")[1]) < 7 && Integer.valueOf(ParticleUtils.version.toLowerCase().split("_")[0].replace("v", "")) == 1) {
            return "net.minecraft.server." + ParticleUtils.version + ".Packet63WorldParticles";
        }
        return "net.minecraft.server." + ParticleUtils.version + ".PacketPlayOutWorldParticles";
    }
    
    static {
        ParticleUtils.version = "";
        try {
            ParticleUtils.version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
            ParticleUtils.packetType = Class.forName(getPacketPlayOutParticles());
            final Class<?> typeCraftPlayer = Class.forName(getCraftPlayerClasspath());
            final Class<?> typeNMSPlayer = Class.forName(getNMSPlayerClasspath());
            final Class<?> typePlayerConnection = Class.forName(getPlayerConnectionClasspath());
            ParticleUtils.getHandle = typeCraftPlayer.getMethod("getHandle", (Class<?>[])new Class[0]);
            ParticleUtils.playerConnection = typeNMSPlayer.getField("playerConnection");
            ParticleUtils.sendPacket = typePlayerConnection.getMethod("sendPacket", Class.forName(getPacketClasspath()));
        }
        catch (Exception e) {
            System.out.println("Failed to setup reflection for PacketPlayOutWorldParticles");
            e.printStackTrace();
        }
    }
}
