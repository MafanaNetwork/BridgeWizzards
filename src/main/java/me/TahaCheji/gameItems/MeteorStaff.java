package me.TahaCheji.gameItems;

import me.TahaCheji.Main;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.itemData.*;
import me.TahaCheji.managers.DamageManager;
import me.TahaCheji.util.AbilityUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

public class MeteorStaff extends MasterItems {


    public MeteorStaff() {
        super("MeteorStaff", Material.ARROW, ItemType.STAFF, RarityType.DIAMOND, true, new MasterAbility("Meteor Strike", AbilityType.RIGHT_CLICK, 150, 15, "Right Click to summon a meteor from above"), false, "I didn't steal it from Terraria I swear");
    }

    @Override
    public void onItemStackCreate(ItemStack var1) {

    }

    @Override
    public boolean leftClickAirAction(Player var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean leftClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean rightClickAirAction(Player player, ItemStack var2) {
        GamePlayer gamePlayer = Main.getInstance().getPlayer(player);
        if (!(gamePlayer.getMana() > getMasterAbility().getManaCost())) {
            player.sendMessage(ChatColor.RED + "You do not have the mana to use this ability");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 10, 1);
            return false;
        }
        new AbilityUtil().sendAbility(player, getMasterAbility());
        gamePlayer.setMana(gamePlayer.getMana() - getMasterAbility().getManaCost());
        new BukkitRunnable() {
            double ti = 0;
            Location loc = player.getLocation().add(0, 10, 0);
            Vector vec = player.getLocation().getDirection().multiply(1.3).setY(-1).normalize();

            public void run() {
                ti++;
                if (ti > 40)
                    cancel();

                loc.add(vec);
                ParticleEffect.EXPLOSION_LARGE.display(loc, 0, 0, 0, 0, 1, null, Bukkit.getOnlinePlayers());
                ParticleEffect.FLAME.display(loc, .2f, .2f, .2f, 0, 4, null, Bukkit.getOnlinePlayers());
                if (loc.getBlock().getType().isSolid()) {
                    loc.add(vec.multiply(-1));
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, 3, .6f);
                    ParticleEffect.EXPLOSION_LARGE.display(loc, 2, 2, 2, 0, 16, null, Bukkit.getOnlinePlayers());
                    ParticleEffect.FLAME.display(loc, 0, 0, 0, .3f, 64, null, Bukkit.getOnlinePlayers());
                    ParticleEffect.EXPLOSION_NORMAL.display(loc, 0, 0, 0, .3f, 32, null, Bukkit.getOnlinePlayers());
                    player.getWorld().createExplosion(loc, 5);
                    cancel();
                    for (Entity target : loc.getNearbyEntities(3, 2, 3)) {
                        if (target.equals(player) || !(target instanceof LivingEntity)) {
                            continue;
                        }
                        new DamageManager(player, (LivingEntity) target, getMasterAbility()).damage();
                        target.setVelocity(target.getLocation().toVector().subtract(loc.toVector()).multiply(.1 * 2).setY(.4 * 3));
                    }
                }
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
        return true;
    }

    @Override
    public boolean rightClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean shiftLeftClickAirAction(Player var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean shiftLeftClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean shiftRightClickAirAction(Player var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean shiftRightClickBlockAction(Player var1, PlayerInteractEvent var2, Block var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean middleClickAction(Player var1, ItemStack var2) {
        return false;
    }

    @Override
    public boolean hitEntityAction(Player var1, EntityDamageByEntityEvent var2, Entity var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean breakBlockAction(Player var1, BlockBreakEvent var2, Block var3, ItemStack var4) {
        return false;
    }

    @Override
    public boolean clickedInInventoryAction(Player var1, InventoryClickEvent var2, ItemStack var3, ItemStack var4) {
        return false;
    }
}
