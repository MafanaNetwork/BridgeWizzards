package me.TahaCheji.managers;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import de.tr7zw.changeme.nbtapi.NBTItem;
import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.itemData.MasterAbility;
import me.TahaCheji.itemData.MasterItems;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class DamageManager implements Listener {

    private final Player damager;
    private final LivingEntity target;
    private final MasterAbility ability;

    public DamageManager(Player damager, LivingEntity target, MasterAbility ability) {
        this.damager = damager;
        this.target = target;
        this.ability = ability;
    }

    public void damage () {
        int damage = ability.getAbilityDamage();
        if(target.getHealth() <= damage && target instanceof Player) {
            Game game = Main.getInstance().getGame((Player) target);
            Player player = (Player) target;
            handle(player, game);
            return;
        }
        target.damage(damage);
        Location loc = target.getLocation().clone().add(getRandomOffset(), 1, getRandomOffset());
        damager.getWorld().spawn(loc, ArmorStand.class, armorStand -> {
            armorStand.setMarker(true);
            armorStand.setVisible(false);
            armorStand.setGravity(false);
            armorStand.setSmall(true);
            armorStand.setCustomNameVisible(true);
            armorStand.setCustomName(ChatColor.DARK_PURPLE + "✧" + damage);
            Bukkit.getScheduler().runTaskLater(Main.getInstance(), armorStand::remove, 20); // Time in ticks (20 ticks = 1 second)
        });
        damager.getWorld().playSound(damager.getLocation(), Sound.ENTITY_EXPERIENCE_BOTTLE_THROW, 2, 1);
    }


    public Player getDamager() {
        return damager;
    }

    public LivingEntity getTarget() {
        return target;
    }

    public MasterAbility getAbility() {
        return ability;
    }
    private double getRandomOffset() {
        double random = Math.random();
        if (Math.random() > 0.5) random *= -1;
        return random;
    }

    private void handle(Player player, Game game) {
        if (!game.isState(Game.GameState.ACTIVE) && !game.isState(Game.GameState.DEATHMATCH)) {
            return;
        }
        GamePlayer gamePlayer = game.getGamePlayer(player);
        game.getGamePlayer(player).setLives(game.getGamePlayer(player).getLives() - 1);
        gamePlayer.getPlayer().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 10);
        if (game.getGamePlayer(player).getLives() <= 0) {
            game.getPlayers().remove(gamePlayer);
            GamePlayer winner = game.getPlayers().get(0);
            game.stopGame();
            game.sendMessage(winner.getName() + " won the Game!");
            Economy econ = Main.getEcon();
            econ.depositPlayer(winner.getPlayer(), 100);
            game.setState(Game.GameState.ENDING);
        } else {
            if (game.isP1(player)) {
                game.getP1().teleport(game.getP1Location());
                game.getP1().getPlayer().setHealth(20);
                game.getP1().getPlayer().setFoodLevel(20);
            } else if (game.isP2(player)) {
                game.getP2().teleport(game.getP2Location());
                game.getP2().getPlayer().setHealth(20);
                game.getP2().getPlayer().setFoodLevel(20);
            } else {
                System.out.println(game.getName() + " SpawnPoint Error");
            }
        }
    }

}
