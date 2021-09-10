package me.TahaCheji.scoreboards;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class LobbyScoreBoard {

    public static void setLobbyScoreBoard(GamePlayer player) {
        Economy econ = Main.getEcon();
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BridgeWizzards", "dummy", ChatColor.GRAY + "♧" + ChatColor.GREEN + "BridgeWizzards" + ChatColor.GRAY + "♧");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score name = obj.getScore(ChatColor.GOLD + "=-=-=-=-BridgeWizzards=-=-=--=");
        name.setScore(16);

        Score emptyText1 = obj.getScore(" ");
        emptyText1.setScore(15);

        Team gameInfo = board.registerNewTeam("Coins");
        gameInfo.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
        gameInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + " Coins: " + econ.getBalance(player.getPlayer()));
        obj.getScore(ChatColor.BLACK + "" + ChatColor.BLACK).setScore(14);

        Score emptyText2 = obj.getScore("  ");
        emptyText2.setScore(13);

        Team playerInfo = board.registerNewTeam("Online");
        playerInfo.addEntry(ChatColor.BLACK + "" + ChatColor.GREEN);
        playerInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "OnlinePlayers: " + Bukkit.getOnlinePlayers().size());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.GREEN).setScore(12);

        Score emptyText3 = obj.getScore("   ");
        emptyText3.setScore(11);

        Score score7 = obj.getScore(ChatColor.GRAY + "Mafana.us.to");
        score7.setScore(10);

        player.getPlayer().setScoreboard(board);
    }

    public static void updateScoreBoard(GamePlayer gamePlayer) {
        Economy econ = Main.getEcon();
        Player player = gamePlayer.getPlayer();
        Scoreboard board = player.getPlayer().getScoreboard();
        board.getTeam("Coins").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + " Coins: " + econ.getBalance(player.getPlayer()));
        board.getTeam("Online").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "OnlinePlayers: " + Bukkit.getOnlinePlayers().size());
    }

}
