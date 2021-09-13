package me.TahaCheji.events;

import io.papermc.paper.event.block.BlockBreakBlockEvent;
import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Game game = Main.getInstance().getGame(player);
        if (game != null) {
            if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING) || game.isState(Game.GameState.PREPARATION) || game.isState(Game.GameState.ENDING)) {
                e.setCancelled(true);
            }
        } else if (!Main.getInstance().isInGame(player)) {
            e.setCancelled(true);
        }

    }


}
