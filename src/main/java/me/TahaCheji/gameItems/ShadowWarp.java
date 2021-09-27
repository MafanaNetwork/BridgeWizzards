package me.TahaCheji.gameItems;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.itemData.*;
import me.TahaCheji.util.AbilityUtil;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.xenondevs.particle.ParticleEffect;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ShadowWarp extends MasterItems implements Listener {


    private List<java.util.UUID> shadowVeil = new ArrayList<UUID>();

    public ShadowWarp() {
        super(null,"ShadowWarp", Material.BLACK_DYE, ItemType.SPELL, RarityType.OBSIDAIN, true, new MasterAbility("Shadow Veil", AbilityType.RIGHT_CLICK, 100, 0, "Right click to turn invisible"), true, "Invis baby");
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
        double duration = 20;
        new AbilityUtil().sendAbility(player, getMasterAbility());
        gamePlayer.setMana(gamePlayer.getMana() - getMasterAbility().getManaCost());
        shadowVeil.add(player.getUniqueId());
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 0);
        for (Player online : Bukkit.getOnlinePlayers())
            online.hidePlayer(player);
        new BukkitRunnable() {
            double ti = 0;
            double y = 0;
            Location loc = player.getLocation();

            public void run() {
                ti++;
                if (ti > duration * 20) {
                    for (Player online : Bukkit.getOnlinePlayers())
                        online.showPlayer(player);
                    shadowVeil.remove(player);
                    ParticleEffect.SMOKE_LARGE.display(player.getLocation().add(0, 1, 0), 0, 0, 0, .13f, 32, null, Bukkit.getOnlinePlayers());
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 0);
                    cancel();
                    return;
                }
                if (!shadowVeil.contains(player.getUniqueId())) {
                    for (Player online : Bukkit.getOnlinePlayers())
                        online.showPlayer(player);
                    ParticleEffect.SMOKE_LARGE.display(player.getLocation().add(0, 1, 0), 0, 0, 0, .13f, 32, null, Bukkit.getOnlinePlayers());
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 3, 0);
                    cancel();
                }
                if (y < 4)
                    for (int j1 = 0; j1 < 5; j1++) {
                        y += .04;
                        for (int j = 0; j < 4; j++) {
                            double xz = y * Math.PI * .8 + (j * Math.PI / 2);
                            ParticleEffect.SMOKE_LARGE.display(loc.clone().add(Math.cos(xz) * 2.5, y, Math.sin(xz) * 2.5), 0, 0, 0, 0, 1, null, Bukkit.getOnlinePlayers());
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

    @EventHandler
    public void a(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player))
            return;

        Player player = (Player) event.getDamager();
        if (shadowVeil.contains(player.getUniqueId()))
            shadowVeil.remove(player.getUniqueId());
    }
}
