package me.TahaCheji.gameItems;

import me.TahaCheji.Main;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.itemData.*;
import me.TahaCheji.util.AbilityUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import xyz.xenondevs.particle.ParticleEffect;

public class Gapple extends MasterItems {


    public Gapple() {
        super("Gapple", Material.YELLOW_DYE, ItemType.SPELL, RarityType.GOLD, true, new MasterAbility("Heal", AbilityType.RIGHT_CLICK, 50, 0, "Right click to heal 5 hearts"), true, "");
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
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 2);
        ParticleEffect.HEART.display(player.getLocation().add(0, .75, 0), 1, 1, 1, 0, 16, null, Bukkit.getOnlinePlayers());
        ParticleEffect.VILLAGER_HAPPY.display(player.getLocation().add(0, .75, 0), 1, 1, 1, 0, 16, null, Bukkit.getOnlinePlayers());
        AbilityUtil.heal(player, 10);
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
