package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.playerData.Levels;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;

public class PlayerLeave implements Listener {


    @EventHandler
    public void onGameLeave(PlayerQuitEvent e) throws IOException {
        Player player = e.getPlayer();
        Game game = Main.getInstance().getGame(player);
        e.setQuitMessage(null);
        if (game != null && game.getGamePlayer(player) != null) {
            game.leaveGame(game.getGamePlayer(player));
        }
        Levels levels = Main.getInstance().getPlayer(player).getLevels();
        levels.setPlayer(Main.getInstance().getPlayer(player));
        levels.saveLvl();
    }

}
