package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerDeath implements Listener {

    @EventHandler
    public void onMoveDeathInGame(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = Main.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if (!(gamePlayer.getPlayerLocation() == PlayerLocation.GAME)) {
                return;
            }
            if (!(event.getTo().getY() <= 0)) {
                return;
            }
            if (gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                handle(event, game);
            }
        }
    }

    @EventHandler
    public void onDamageDeath(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player) event.getEntity();
        Game game = Main.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            if (event.getDamage() >= player.getHealth()) {
                event.setCancelled(true);
                handle(event, game);
            }
        }
    }


    private void handle(PlayerMoveEvent event, Game game) {
        Player player = event.getPlayer();

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

    private void handle(EntityDamageByEntityEvent event, Game game) {
        Player player = (Player) event.getEntity();

        if (!game.isState(Game.GameState.ACTIVE) && !game.isState(Game.GameState.DEATHMATCH)) {
            return;
        }
        GamePlayer gamePlayer = game.getGamePlayer(player);
        game.getGamePlayer(player).setLives(game.getGamePlayer(player).getLives() - 1);
        gamePlayer.getPlayer().playSound(player.getLocation(), Sound.BLOCK_GLASS_BREAK, 10, 10);
        if (game.getGamePlayer(player).getLives() <= 0) {
            game.getPlayers().remove(gamePlayer);
            GamePlayer winner = game.getPlayers().get(0);
            game.sendMessage(winner.getName() + " won the Game!");
            Economy econ = Main.getEcon();
            econ.depositPlayer(winner.getPlayer(), 100);
            game.setState(Game.GameState.ENDING);
            game.stopGame();
        } else {
            if (game.isP1(player)) {
                game.getP1().teleport(game.getP1Location());
            } else if (game.isP2(player)) {
                game.getP2().teleport(game.getP2Location());
            } else {
                System.out.println(game.getName() + " SpawnPoint Error");
            }
        }
    }


}
