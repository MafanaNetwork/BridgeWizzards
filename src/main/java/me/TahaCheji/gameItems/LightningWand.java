package me.TahaCheji.gameItems;

import me.TahaCheji.Main;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.itemData.*;
import me.TahaCheji.managers.DamageManager;
import me.TahaCheji.util.AbilityUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import xyz.xenondevs.particle.ParticleEffect;

public class LightningWand extends MasterItems {


    public LightningWand() {
        super("LightningWand", Material.BONE, ItemType.WAND, RarityType.GOLD, true, new MasterAbility("Sparkle", AbilityType.RIGHT_CLICK, 75, 5), false, "I STRIKE AGAIN MUAHAHAHA");
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
        double limit = 3;
        int count = 0;
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 2, 2);
        for (Entity ent : player.getNearbyEntities(10, 10, 10)) {
            if (ent instanceof LivingEntity && ent != player && !(ent instanceof ArmorStand)) {
                if (count >= limit)
                    break;
                count++;
                new DamageManager(player, (LivingEntity) ent, getMasterAbility()).damage();
                ParticleEffect.EXPLOSION_LARGE.display(ent.getLocation().add(0, 1, 0), 0, 0, 0, 0, 1,null, Bukkit.getOnlinePlayers());
                Location loc_t = player.getLocation().add(0, .75, 0);
                Location loc_ent = ent.getLocation().add(0, .75, 0);
                for (double j1 = 0; j1 < 1; j1 += .04) {
                    Vector d = loc_ent.toVector().subtract(loc_t.toVector());
                    ParticleEffect.FIREWORKS_SPARK.display(loc_t.clone().add(d.multiply(j1)), .1f, .1f, .1f, .008f, 3, null, Bukkit.getOnlinePlayers());
                }
            }
        }
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
