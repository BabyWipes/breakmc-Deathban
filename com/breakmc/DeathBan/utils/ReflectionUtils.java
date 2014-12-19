package com.breakmc.DeathBan.utils;

import java.util.*;
import org.bukkit.entity.*;
import java.lang.reflect.*;
import org.bukkit.*;

public class ReflectionUtils
{
    public static void sendPacketRadius(final Location loc, final int radius, final Object packet) {
        for (final Player p : loc.getWorld().getPlayers()) {
            if (loc.distanceSquared(p.getLocation()) < radius * radius) {
                sendPacket(p, packet);
            }
        }
    }
    
    public static void sendPacket(final List<Player> players, final Object packet) {
        for (final Player p : players) {
            sendPacket(p, packet);
        }
    }
    
    public static void sendPacket(final Player p, final Object packet) {
        try {
            final Object nmsPlayer = getHandle((Entity)p);
            final Field con_field = nmsPlayer.getClass().getField("playerConnection");
            final Object con = con_field.get(nmsPlayer);
            final Method packet_method = getMethod(con.getClass(), "sendPacket");
            packet_method.invoke(con, packet);
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (IllegalArgumentException e2) {
            e2.printStackTrace();
        }
        catch (IllegalAccessException e3) {
            e3.printStackTrace();
        }
        catch (InvocationTargetException e4) {
            e4.printStackTrace();
        }
        catch (NoSuchFieldException e5) {
            e5.printStackTrace();
        }
    }
    
    public static Class<?> getCraftClass(final String ClassName) {
        final String name = Bukkit.getServer().getClass().getPackage().getName();
        final String version = name.substring(name.lastIndexOf(46) + 1) + ".";
        final String className = "net.minecraft.server." + version + ClassName;
        Class<?> c = null;
        try {
            c = Class.forName(className);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return c;
    }
    
    public static Object getHandle(final Entity entity) {
        Object nms_entity = null;
        final Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity, new Object[0]);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
        return nms_entity;
    }
    
    public static Object getHandle(final Object entity) {
        Object nms_entity = null;
        final Method entity_getHandle = getMethod(entity.getClass(), "getHandle");
        try {
            nms_entity = entity_getHandle.invoke(entity, new Object[0]);
        }
        catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        catch (IllegalAccessException e2) {
            e2.printStackTrace();
        }
        catch (InvocationTargetException e3) {
            e3.printStackTrace();
        }
        return nms_entity;
    }
    
    public static Field getField(final Class<?> cl, final String field_name) {
        try {
            final Field field = cl.getDeclaredField(field_name);
            return field;
        }
        catch (SecurityException e) {
            e.printStackTrace();
        }
        catch (NoSuchFieldException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cl, final String method, final Class<?>[] args) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method) && ClassListEqual(args, m.getParameterTypes())) {
                return m;
            }
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cl, final String method, final Integer args) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method) && args.equals(m.getParameterTypes().length)) {
                return m;
            }
        }
        return null;
    }
    
    public static Method getMethod(final Class<?> cl, final String method) {
        for (final Method m : cl.getMethods()) {
            if (m.getName().equals(method)) {
                return m;
            }
        }
        return null;
    }
    
    public static void setValue(final Object instance, final String fieldName, final Object value) throws Exception {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(instance, value);
    }
    
    public static Object getValue(final Object instance, final String fieldName) throws Exception {
        final Field field = instance.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(instance);
    }
    
    public static boolean ClassListEqual(final Class<?>[] l1, final Class<?>[] l2) {
        boolean equal = true;
        if (l1.length != l2.length) {
            return false;
        }
        for (int i = 0; i < l1.length; ++i) {
            if (l1[i] != l2[i]) {
                equal = false;
                break;
            }
        }
        return equal;
    }
}
