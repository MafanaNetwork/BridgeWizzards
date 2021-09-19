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
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;

public class Earthquake extends MasterItems {


    public Earthquake() {
        super("Earthquake", Material.BROWN_DYE, ItemType.SPELL, RarityType.LAPIS, true,
                new MasterAbility("One With The Earth", AbilityType.RIGHT_CLICK, 350, 18, "Right Click to create a Earthquake"), true, "Rumble ruble ruble");
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
        if(!(gamePlayer.getMana() > getMasterAbility().getManaCost())) {
            player.sendMessage(ChatColor.RED + "You do not have the mana to use this ability");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_HURT, 10, 1);
            return false;
        }
        new AbilityUtil().sendAbility(player, getMasterAbility());
        gamePlayer.setMana(gamePlayer.getMana() - getMasterAbility().getManaCost());
        new BukkitRunnable() {
            Vector vec = new AbilityUtil().getTargetDirection(player,null).setY(0);
            Location loc = player.getLocation().clone();
            int ti = 0;
            List<Integer> hit = new ArrayList<>();

            public void run() {
                ti++;
                if (ti > 20)
                    cancel();

                loc.add(vec);
                ParticleEffect.CLOUD.display(loc, .5f, 0, .5f, 0, 5, null, Bukkit.getOnlinePlayers());
                loc.getWorld().playSound(loc, Sound.BLOCK_GRAVEL_BREAK, 2, 1);
                for (Entity target : loc.getNearbyEntities(3, 3, 3))
                    if (loc.distanceSquared(target.getLocation()) < 2 && !hit.contains(target.getEntityId()) && !target.equals(player)) {
                        hit.add(target.getEntityId());
                        new DamageManager(player, (LivingEntity) target, getMasterAbility()).damage();
                        ((LivingEntity) target).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int) (5 * 20), (int) 5));
                        cancel();
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
