package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {



    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GamePlayer gamePlayer = new GamePlayer(e.getPlayer(), PlayerLocation.LOBBY);
        Main.players.add(gamePlayer);
        e.getPlayer().teleport(Main.getInstance().getLobbyPoint());
        e.setJoinMessage(null);
        e.getPlayer().setScoreboard(null);
        LobbyScoreBoard.setLobbyScoreBoard(gamePlayer);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
            if(!gamePlayer.getPlayer().isOnline()) {
                return;
            }
            LobbyScoreBoard.updateScoreBoard(gamePlayer);
            }
        }, 0, 5);
    }


}
