package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        Game game = Main.getInstance().getGame(player);
        if (game != null) {
            if (game.isMovementFrozen()) {
                if (event.getFrom().getBlockX() != event.getTo().getBlockX() || event.getFrom().getBlockZ() != event.getTo().getBlockZ()) {
                    player.teleport(event.getFrom());
                }
            }
        }
    }

}
