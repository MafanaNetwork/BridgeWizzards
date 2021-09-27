package me.TahaCheji.events;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.playerData.PlayerLevels;
import me.TahaCheji.playerData.PlayerLocation;
import me.TahaCheji.playerData.Levels;
import me.TahaCheji.playerData.LevelData;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import org.bukkit.GameMode;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.io.File;
import java.io.IOException;

public class PlayerJoin implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e) throws IOException {
        GamePlayer gamePlayer = new GamePlayer(e.getPlayer(), PlayerLocation.LOBBY);
        if(!Main.players.contains(gamePlayer)) {
            Main.getInstance().addGamePlayer(gamePlayer);
        }
        Player p = gamePlayer.getPlayer();

        Levels levels = new Levels(gamePlayer, 0, 0);
        File playerData = new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString() + "/data.yml");
        FileConfiguration pD = YamlConfiguration.loadConfiguration(playerData);
        if (!new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString()).exists()) {
            new File("plugins/BridgeWiz/playerData/" + p.getUniqueId().toString()).mkdir();
        }
        if (!playerData.exists()) {
            playerData.createNewFile();
            pD.set("info.playerUUID", levels.getPlayer().getPlayer().getUniqueId().toString());
            pD.set("info.lvl", levels.getLevel());
            pD.set("info.xp", levels.getXp());
            pD.save(playerData);
        }
        Levels playerLvl = LevelData.getPlayerLevel(gamePlayer.getPlayer());
        gamePlayer.setLevels(playerLvl);

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
