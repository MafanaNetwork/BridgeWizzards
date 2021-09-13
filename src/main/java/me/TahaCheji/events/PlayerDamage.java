package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

public class PlayerDamage implements Listener {


    @EventHandler
    public void lobbyDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getEntity();
        Game game = Main.getInstance().getGame(player);
        if (game != null) {
            if (game.isState(Game.GameState.LOBBY) || game.isState(Game.GameState.STARTING) || game.isState(Game.GameState.PREPARATION) || game.isState(Game.GameState.ENDING) || e.getCause() == EntityDamageEvent.DamageCause.FALL) {
                e.setCancelled(true);
            }
        } else if (!Main.getInstance().isInGame(player)) {
            e.setCancelled(true);
        }

    }
}