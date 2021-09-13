package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoin implements Listener {

    int taskID;

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        GamePlayer gamePlayer = new GamePlayer(e.getPlayer(), PlayerLocation.LOBBY);
        Main.players.add(gamePlayer);
        gamePlayer.getPlayer().setHealth(20);
        gamePlayer.getPlayer().setFoodLevel(20);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().getInventory().setArmorContents(null);
        e.getPlayer().teleport(Main.getInstance().getLobbyPoint());
        e.setJoinMessage(null);
        e.getPlayer().setGameMode(GameMode.ADVENTURE);
        LobbyScoreBoard lobbyScoreBoard = new LobbyScoreBoard();
        lobbyScoreBoard.setLobbyScoreBoard(gamePlayer);
        lobbyScoreBoard.updateScoreBoard(gamePlayer);
        //give them the lobby stuff
    }


}
