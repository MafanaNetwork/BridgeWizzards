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

   public int TaskId;
   public Scoreboard board;

    public void setLobbyScoreBoard(GamePlayer player) {
        Economy econ = Main.getEcon();
        board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BridgeWizzards", "dummy", ChatColor.GRAY + "♧" + ChatColor.GOLD + "BridgeWizzards" + ChatColor.GRAY + "♧");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score name = obj.getScore(ChatColor.GOLD + "=-=-=-=-=-=-=-=-=-=-=-=-=--=");
        name.setScore(16);

        Score emptyText1 = obj.getScore(" ");
        emptyText1.setScore(15);

        Team gameInfo = board.registerNewTeam("Coins");
        gameInfo.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
        gameInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Coins: " + econ.getBalance(player.getPlayer()));
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

    public void updateScoreBoard(GamePlayer gamePlayer) {
       TaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                Economy econ = Main.getEcon();
                Player player = gamePlayer.getPlayer();
                if(!player.isOnline()) {
                    stopUpdating();
                    return;
                }
                Scoreboard newBoard = board;
                newBoard.getTeam("Coins").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Coins: " + econ.getBalance(player.getPlayer()));
                newBoard.getTeam("Online").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "OnlinePlayers: " + Bukkit.getOnlinePlayers().size());
            }
        }, 0, 5);
    }

    public void stopUpdating() {
        Bukkit.getScheduler().cancelTask(TaskId);
    }

    public int getTaskId() {
        return TaskId;
    }
}
