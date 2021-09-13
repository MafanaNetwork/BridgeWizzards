package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerLobbyMove implements Listener {

    @EventHandler
    public void lobbyMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (GamePlayer gamePlayer : Main.players) {
            if (gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                if(Main.getInstance().isInGame(player)) {
                    continue;
                }
                if (gamePlayer.getPlayerLocation() != PlayerLocation.LOBBY) {
                    continue;
                }
                if (!(event.getTo().getY() <= 10)) {
                    continue;
                }
                if (gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                    gamePlayer.teleport(Main.getInstance().getLobbyPoint());
                }
            }
        }
    }

    @EventHandler
    public void gameLobbyMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Game game = Main.getInstance().getGame(player);
        if (game != null && game.getGamePlayer(player) != null) {
            GamePlayer gamePlayer = game.getGamePlayer(player);
            if(!Main.getInstance().isInGame(player)) {
                return;
            }
            if (gamePlayer.getPlayerLocation() != PlayerLocation.GAMELOBBY) {
                return;
            }
            if (!(event.getTo().getY() <= 0)) {
                return;
            }
            if (gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                player.teleport(game.getLobbySpawn());
                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_PEARL_THROW, 10, 1);
            }
        }
    }


}
