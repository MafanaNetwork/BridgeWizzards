package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeave implements Listener {


    @EventHandler
    public void onGameLeave(PlayerQuitEvent e) {
        Player player = e.getPlayer();
        Game game = Main.getInstance().getGame(player);
        e.setQuitMessage(null);
        if (game != null && game.getGamePlayer(player) != null) {
            game.leaveGame(game.getGamePlayer(player));
        }
    }

}
