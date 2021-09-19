package me.TahaCheji.util;

import me.TahaCheji.Main;
import me.TahaCheji.itemData.MasterAbility;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AbilityUtil {

    private static final Random random = new Random();

    public static void saturate(Player player, double saturation) {
        if (saturation <= 0)
            return;

        float saturated = player.getSaturation() + (float) saturation;
        player.setSaturation(saturated > 20 ? 20f : saturated);
    }

    public static void feed(Player player, int feed) {
        if (feed <= 0)
            return;

        int food = player.getFoodLevel() + feed;
        player.setFoodLevel(food > 20 ? 20 : food);
    }

    @SuppressWarnings("deprecation")
    public static void heal(Player player, double heal) {
        if (heal <= 0)
            return;

        double health = player.getHealth() + heal;
        player.setHealth(health > player.getMaxHealth() ? player.getMaxHealth() : health);
    }

    public static PotionEffect getPotionEffect(Player player, PotionEffectType type) {
        for (PotionEffect effect : player.getActivePotionEffects())
            if (effect.getType() == type)
                return effect;
        return null;
    }

    public static String intToRoman(int input) {
        if (input < 1)
            return "0";
        if (input > 499)
            return ">499";

        String s = "";
        while (input >= 1000) {
            s += "M";
            input -= 1000;
        }
        while (input >= 900) {
            s += "DM";
            input -= 900;
        }
        while (input >= 500) {
            s += "D";
            input -= 500;
        }
        while (input >= 400) {
            s += "CD";
            input -= 400;
        }
        while (input >= 100) {
            s += "C";
            input -= 100;
        }
        while (input >= 90) {
            s += "XC";
            input -= 90;
        }
        while (input >= 50) {
            s += "L";
            input -= 50;
        }
        while (input >= 40) {
            s += "XL";
            input -= 40;
        }
        while (input >= 10) {
            s += "X";
            input -= 10;
        }
        while (input >= 9) {
            s += "IX";
            input -= 9;
        }
        while (input >= 5) {
            s += "V";
            input -= 5;
        }
        while (input >= 4) {
            s += "IV";
            input -= 4;
        }
        while (input >= 1) {
            s += "I";
            input -= 1;
        }
        return s;
    }

    public static double truncation(double x, int n) {
        double pow = Math.pow(10.0, n);
        return Math.floor(x * pow) / pow;
    }

    // returns random value if supported OR single value
    public static double randomValue(String string) {
        String[] split = string.split("\\=");
        if (split.length == 2)
            try {
                double[] values = new double[] { Double.parseDouble(split[0]), Double.parseDouble(split[1]) };
                return random.nextDouble() * (values[1] - values[0]) + values[0];
            } catch (Exception e) {
            }
        return Double.parseDouble(string);
    }

    public static Vector rotAxisX(Vector v, double a) {
        double y = v.getY() * Math.cos(a) - v.getZ() * Math.sin(a);
        double z = v.getY() * Math.sin(a) + v.getZ() * Math.cos(a);
        return v.setY(y).setZ(z);
    }

    public static Vector rotAxisY(Vector v, double b) {
        double x = v.getX() * Math.cos(b) + v.getZ() * Math.sin(b);
        double z = v.getX() * -Math.sin(b) + v.getZ() * Math.cos(b);
        return v.setX(x).setZ(z);
    }

    public static Vector rotAxisZ(Vector v, double c) {
        double x = v.getX() * Math.cos(c) - v.getY() * Math.sin(c);
        double y = v.getX() * Math.sin(c) + v.getY() * Math.cos(c);
        return v.setX(x).setY(y);
    }

    public static Vector rotateFunc(Vector v, Location loc) {
        double yaw = loc.getYaw() / 180 * Math.PI;
        double pitch = loc.getPitch() / 180 * Math.PI;
        v = rotAxisX(v, pitch);
        v = rotAxisY(v, -yaw);
        return v;
    }

    public static List<org.bukkit.entity.Entity> getNearbyChunkEntities(Location loc) {

        /*
         * another method to save performance is if an entit bounding box
         * calculation is made twice in the same tick then the method does not
         * need to be called twice, it can utilize the same entity list since
         * the entities have not moved (e.g fireball which does 2+ calculations
         * per tick)
         */
        List<Entity> entities = new ArrayList<Entity>();

        int cx = loc.getChunk().getX();
        int cz = loc.getChunk().getZ();

        for (int x = -1; x < 2; x++)
            for (int z = -1; z < 2; z++)
                for (Entity entity : loc.getWorld().getChunkAt(cx + x, cz + z).getEntities())
                    entities.add(entity);

        return entities;
    }

    public static boolean canDamage(Player player, Location loc, Entity target) {
        if (target.equals(player) || !(target instanceof LivingEntity) || target instanceof ArmorStand || target.isDead())
            return false;

        if (target.hasMetadata("NPC"))
            return false;

        if (target instanceof Player)
            return false;

        return loc == null;
    }

    public Vector getTargetDirection(Player player, LivingEntity target) {
        return target == null ? player.getEyeLocation().getDirection() : target.getLocation().add(0, 1, 0).subtract(player.getLocation().add(0, 1.3, 0)).toVector().normalize();
    }

    public void sendAbility(Player player, MasterAbility masterAbility) {
        BukkitTask t = new BukkitRunnable() {
            @Override
            public void run() {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "-" + masterAbility.getManaCost() + " Mana " + ChatColor.GOLD + "[" + masterAbility.getName() + "]"));
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
        new BukkitRunnable() {
            @Override
            public void run() {
                t.cancel();
            }
        }.runTaskLaterAsynchronously(Main.getInstance(), 20L);

    }


}
