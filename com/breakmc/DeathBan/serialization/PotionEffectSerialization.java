package com.breakmc.DeathBan.serialization;

import java.util.*;
import org.bukkit.potion.*;
import org.bukkit.entity.*;

public class PotionEffectSerialization
{
    public static String serializeEffects(final Collection<PotionEffect> effects) {
        String serialized = "";
        for (final PotionEffect e : effects) {
            serialized = serialized + e.getType().getId() + ":" + e.getDuration() + ":" + e.getAmplifier() + ";";
        }
        return serialized;
    }
    
    public static Collection<PotionEffect> getPotionEffects(final String serializedEffects) {
        final ArrayList<PotionEffect> effects = new ArrayList<PotionEffect>();
        if (serializedEffects.isEmpty()) {
            return effects;
        }
        final String[] effs = serializedEffects.split(";");
        for (int i = 0; i < effs.length; ++i) {
            final String[] effect = effs[i].split(":");
            if (effect.length < 3) {
                throw new IllegalArgumentException(serializedEffects + " - PotionEffect " + i + " (" + effs[i] + "): split must at least have a length of 3");
            }
            if (!Util.isNum(effect[0])) {
                throw new IllegalArgumentException(serializedEffects + " - PotionEffect " + i + " (" + effs[i] + "): id is not an integer");
            }
            if (!Util.isNum(effect[1])) {
                throw new IllegalArgumentException(serializedEffects + " - PotionEffect " + i + " (" + effs[i] + "): duration is not an integer");
            }
            if (!Util.isNum(effect[2])) {
                throw new IllegalArgumentException(serializedEffects + " - PotionEffect " + i + " (" + effs[i] + "): amplifier is not an integer");
            }
            final int id = Integer.parseInt(effect[0]);
            final int duration = Integer.parseInt(effect[1]);
            final int amplifier = Integer.parseInt(effect[2]);
            final PotionEffectType effectType = PotionEffectType.getById(id);
            if (effectType == null) {
                throw new IllegalArgumentException(serializedEffects + " - PotionEffect " + i + " (" + effs[i] + "): no PotionEffectType with id of " + id);
            }
            final PotionEffect e = new PotionEffect(effectType, duration, amplifier);
            effects.add(e);
        }
        return effects;
    }
    
    public static void addPotionEffects(final String code, final LivingEntity entity) {
        entity.addPotionEffects((Collection)getPotionEffects(code));
    }
    
    public static void setPotionEffects(final String code, final LivingEntity entity) {
        for (final PotionEffect effect : entity.getActivePotionEffects()) {
            entity.removePotionEffect(effect.getType());
        }
        addPotionEffects(code, entity);
    }
}
