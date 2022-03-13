//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.thejokerdev.creator.utils;

import com.google.common.base.Strings;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public enum XPotion {
    ABSORPTION(new String[]{"ABSORB"}),
    BAD_OMEN(new String[]{"OMEN_BAD", "PILLAGER"}),
    BLINDNESS(new String[]{"BLIND"}),
    CONDUIT_POWER(new String[]{"CONDUIT", "POWER_CONDUIT"}),
    CONFUSION(new String[]{"NAUSEA", "SICKNESS", "SICK"}),
    DAMAGE_RESISTANCE(new String[]{"RESISTANCE", "ARMOR", "DMG_RESIST", "DMG_RESISTANCE"}),
    DOLPHINS_GRACE(new String[]{"DOLPHIN", "GRACE"}),
    FAST_DIGGING(new String[]{"HASTE", "SUPER_PICK", "DIGFAST", "DIG_SPEED", "QUICK_MINE", "SHARP"}),
    FIRE_RESISTANCE(new String[]{"FIRE_RESIST", "RESIST_FIRE", "FIRE_RESISTANCE"}),
    GLOWING(new String[]{"GLOW", "SHINE", "SHINY"}),
    HARM(new String[]{"INJURE", "DAMAGE", "HARMING", "INFLICT"}),
    HEAL(new String[]{"HEALTH", "INSTA_HEAL", "INSTANT_HEAL", "INSTA_HEALTH", "INSTANT_HEALTH"}),
    HEALTH_BOOST(new String[]{"BOOST_HEALTH", "BOOST", "HP"}),
    HERO_OF_THE_VILLAGE(new String[]{"HERO", "VILLAGE_HERO"}),
    HUNGER(new String[]{"STARVE", "HUNGRY"}),
    INCREASE_DAMAGE(new String[]{"STRENGTH", "BULL", "STRONG", "ATTACK"}),
    INVISIBILITY(new String[]{"INVISIBLE", "VANISH", "INVIS", "DISAPPEAR", "HIDE"}),
    JUMP(new String[]{"LEAP", "JUMP_BOOST"}),
    LEVITATION(new String[]{"LEVITATE"}),
    LUCK(new String[]{"LUCKY"}),
    NIGHT_VISION(new String[]{"VISION", "VISION_NIGHT"}),
    POISON(new String[]{"VENOM"}),
    REGENERATION(new String[]{"REGEN"}),
    SATURATION(new String[]{"FOOD"}),
    SLOW(new String[]{"SLOWNESS", "SLUGGISH"}),
    SLOW_DIGGING(new String[]{"FATIGUE", "DULL", "DIGGING", "SLOW_DIG", "DIG_SLOW"}),
    SLOW_FALLING(new String[]{"SLOW_FALL", "FALL_SLOW"}),
    SPEED(new String[]{"SPRINT", "RUNFAST", "SWIFT", "FAST"}),
    UNLUCK(new String[]{"UNLUCKY"}),
    WATER_BREATHING(new String[]{"WATER_BREATH", "UNDERWATER_BREATHING", "UNDERWATER_BREATH", "AIR"}),
    WEAKNESS(new String[]{"WEAK"}),
    WITHER(new String[]{"DECAY"});

    public static final XPotion[] VALUES = values();
    public static final Set<XPotion> DEBUFFS = Collections.unmodifiableSet(EnumSet.of(BAD_OMEN, BLINDNESS, CONFUSION, HARM, HUNGER, LEVITATION, POISON, SLOW, SLOW_DIGGING, UNLUCK, WEAKNESS, WITHER));
    private final PotionEffectType type = PotionEffectType.getByName(this.name());

    private XPotion(@Nonnull String... aliases) {
        Data.NAMES.put(this.name(), this);
        String[] var4 = aliases;
        int var5 = aliases.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            String legacy = var4[var6];
            Data.NAMES.put(legacy, this);
        }

    }

    @Nonnull
    private static String format(@Nonnull String name) {
        int len = name.length();
        char[] chs = new char[len];
        int count = 0;
        boolean appendUnderline = false;

        for(int i = 0; i < len; ++i) {
            char ch = name.charAt(i);
            if (!appendUnderline && count != 0 && (ch == '-' || ch == ' ' || ch == '_') && chs[count] != '_') {
                appendUnderline = true;
            } else if (ch >= 'A' && ch <= 'Z' || ch >= 'a' && ch <= 'z') {
                if (appendUnderline) {
                    chs[count++] = '_';
                    appendUnderline = false;
                }

                chs[count++] = (char)(ch & 95);
            }
        }

        return new String(chs, 0, count);
    }

    @Nonnull
    public static Optional<XPotion> matchXPotion(@Nonnull String potion) {
        Validate.notEmpty(potion, "Cannot match XPotion of a null or empty potion effect type");
        PotionEffectType idType = getIdFromString(potion);
        if (idType != null) {
            XPotion type = (XPotion) Data.NAMES.get(idType.getName());
            if (type == null) {
                throw new NullPointerException("Unsupported potion effect type ID: " + idType);
            } else {
                return Optional.of(type);
            }
        } else {
            return Optional.ofNullable((XPotion) Data.NAMES.get(format(potion)));
        }
    }

    @Nonnull
    public static XPotion matchXPotion(@Nonnull PotionEffectType type) {
        Objects.requireNonNull(type, "Cannot match XPotion of a null potion effect type");
        return (XPotion)Objects.requireNonNull((XPotion) Data.NAMES.get(type.getName()), () -> {
            return "Unsupported potion effect type: " + type.getName();
        });
    }

    @Nullable
    private static PotionEffectType getIdFromString(@Nonnull String type) {
        try {
            int id = Integer.parseInt(type);
            return PotionEffectType.getById(id);
        } catch (NumberFormatException var2) {
            return null;
        }
    }

    @Nullable
    public static PotionEffect parsePotionEffectFromString(@Nullable String potion) {
        if (!Strings.isNullOrEmpty(potion) && !potion.equalsIgnoreCase("none")) {
            String[] split = StringUtils.split(StringUtils.deleteWhitespace(potion), ',');
            if (split.length == 0) {
                split = StringUtils.split(potion, ' ');
            }

            Optional<XPotion> typeOpt = matchXPotion(split[0]);
            if (!typeOpt.isPresent()) {
                return null;
            } else {
                PotionEffectType type = ((XPotion)typeOpt.get()).parsePotionEffectType();
                if (type == null) {
                    return null;
                } else {
                    int duration = 2400;
                    int amplifier = 0;
                    if (split.length > 1) {
                        duration = NumberUtils.toInt(split[1]) * 20;
                        if (split.length > 2) {
                            amplifier = NumberUtils.toInt(split[2]) - 1;
                        }
                    }

                    return new PotionEffect(type, duration, amplifier);
                }
            }
        } else {
            return null;
        }
    }

    public static void addPotionEffectsFromString(@Nonnull Player player, @Nullable List<String> effects) {
        if (effects != null && !effects.isEmpty()) {
            Objects.requireNonNull(player, "Cannot add potion effects to null player");
            Iterator var2 = effects.iterator();

            while(var2.hasNext()) {
                String effect = (String)var2.next();
                PotionEffect potionEffect = parsePotionEffectFromString(effect);
                if (potionEffect != null) {
                    player.addPotionEffect(potionEffect);
                }
            }

        }
    }

    @Nonnull
    public static ThrownPotion throwPotion(@Nonnull LivingEntity entity, @Nullable Color color, @Nullable PotionEffect... effects) {
        Objects.requireNonNull(entity, "Cannot throw potion from null entity");
        ItemStack potion = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta)potion.getItemMeta();
        if (effects != null) {
            PotionEffect[] var5 = effects;
            int var6 = effects.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                PotionEffect effect = var5[var7];
                meta.addCustomEffect(effect, true);
            }
        }

        potion.setItemMeta(meta);
        ThrownPotion thrownPotion = (ThrownPotion)entity.launchProjectile(ThrownPotion.class);
        thrownPotion.setItem(potion);
        return thrownPotion;
    }

    @Nonnull
    public static ItemStack buildItemWithEffects(@Nonnull Material type, @Nullable Color color, @Nullable PotionEffect... effects) {
        Objects.requireNonNull(type, "Cannot build an effected item with null type");
        Validate.isTrue(canHaveEffects(type), "Cannot build item with " + type.name() + " potion type");
        ItemStack item = new ItemStack(type);
        PotionMeta meta = (PotionMeta)item.getItemMeta();
        if (effects != null) {
            PotionEffect[] var5 = effects;
            int var6 = effects.length;

            for(int var7 = 0; var7 < var6; ++var7) {
                PotionEffect effect = var5[var7];
                meta.addCustomEffect(effect, true);
            }
        }

        item.setItemMeta(meta);
        return item;
    }

    public static boolean canHaveEffects(@Nullable Material material) {
        return material != null && (material.name().endsWith("POTION") || material.name().startsWith("TIPPED_ARROW"));
    }

    @Nullable
    public PotionEffectType parsePotionEffectType() {
        return this.type;
    }

    public boolean isSupported() {
        return this.parsePotionEffectType() != null;
    }

    /** @deprecated */
    @Nullable
    @Deprecated
    public PotionType getPotionType() {
        PotionEffectType type = this.parsePotionEffectType();
        return type == null ? null : PotionType.getByEffect(type);
    }

    @Nullable
    public PotionEffect parsePotion(int duration, int amplifier) {
        PotionEffectType type = this.parsePotionEffectType();
        return type == null ? null : new PotionEffect(type, duration, amplifier);
    }

    public String toString() {
        return WordUtils.capitalize(this.name().replace('_', ' ').toLowerCase(Locale.ENGLISH));
    }

    private static final class Data {
        private static final Map<String, XPotion> NAMES = new HashMap();

        private Data() {
        }
    }
}
