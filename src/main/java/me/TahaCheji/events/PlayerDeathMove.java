package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerDeathMove implements Listener {

    @EventHandler
    public void onDeathInGame(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = Main.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if(!(gamePlayer.getPlayerLocation() == PlayerLocation.GAME)) {
                return;
            }
            if(!(event.getTo().getY() <= 15)) {
                return;
            }
            if (gamePlayer.getPlayer() == player) {
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
        if (game.getGamePlayer(player).getLives() <= 0) {
            game.getPlayers().remove(player);
            GamePlayer winner = game.getPlayers().get(0);
            game.sendMessage(winner.getName() + " won the Game!");
            Economy econ = Main.getEcon();
            econ.depositPlayer(winner.getPlayer(), 100);
            game.setState(Game.GameState.ENDING);
            game.stopGame();
        } else  {
            if(game.isP1(player)) {
                game.getP1().teleport(game.getP1Location());
            } else if(game.isP2(player)) {
                game.getP2().teleport(game.getP2Location());
            } else {
                System.out.println(game.getName() + " SpawnPoint Error");
            }
        }
    }


}
