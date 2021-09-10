package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerLobbyMove implements Listener {

    @EventHandler
    public void onDeathInGame(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for(GamePlayer gamePlayer : Main.players) {
            if(gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                if (!(gamePlayer.getPlayerLocation() == PlayerLocation.GAME)) {
                    return;
                }
                if (!(event.getTo().getY() <= 15)) {
                    return;
                }
                if (gamePlayer.getPlayer() == player) {
                    gamePlayer.teleport(Main.getInstance().getLobbyPoint());
                }
            }
        }
    }


}
