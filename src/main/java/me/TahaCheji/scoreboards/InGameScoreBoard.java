package me.TahaCheji.scoreboards;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

import java.util.List;

public class InGameScoreBoard {

    public int TaskId;

    public void setGameScoreboard(GamePlayer player) {
        Game game = Main.getInstance().getGame(player.getPlayer());
        game.setGameTime(300);
        player.manaRegen();
        Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective obj = board.registerNewObjective("BridgeWizzards", "dummy", ChatColor.GRAY + "♧" + ChatColor.GOLD + "BridgeWizzards" + ChatColor.GRAY + "♧");
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        Score name = obj.getScore(ChatColor.GOLD + "=-=-=-=-=-BridgeWizzards-=-=-=-=-=");
        name.setScore(16);

        Score emptyText1 = obj.getScore(" ");
        emptyText1.setScore(15);

        Team gameInfo = board.registerNewTeam("GameInfo");
        gameInfo.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
        gameInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Name: " + game.getName() + " | Mode: " + game.getGameMode().toString() + " | Map: " + game.getMap().getName());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.BLACK).setScore(14);

        Score emptyText2 = obj.getScore("   ");
        emptyText2.setScore(13);

        Team time = board.registerNewTeam("GameTime");
        time.addEntry(ChatColor.BLACK + "" + ChatColor.GOLD);
        time.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + game.getGameTime());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.GOLD).setScore(12);
        System.out.println(game.getGameTime());

        Score emptyText3 = obj.getScore("    ");
        emptyText3.setScore(11);

        Team playerInfo = board.registerNewTeam("PlayerInfo");
        playerInfo.addEntry(ChatColor.BLACK + "" + ChatColor.GREEN);
        playerInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Mana: " + player.getMana() + " | Lives: " + player.getLives());
        obj.getScore(ChatColor.BLACK + "" + ChatColor.GREEN).setScore(10);

        Score emptyText4 = obj.getScore("      ");
        emptyText4.setScore(9);

        Score score7 = obj.getScore(ChatColor.GRAY + "Mafana.us.to");
        score7.setScore(8);

        player.getPlayer().setScoreboard(board);
    }

    public void updateScoreBoard(Game game) {
       TaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), new Runnable() {
            @Override
            public void run() {
                for (GamePlayer player : game.getPlayers()) {
                    if(!player.getPlayer().isOnline()) {
                        stopUpdating();
                        return;
                    }
                    Scoreboard board = player.getPlayer().getScoreboard();
                    board.getTeam("PlayerInfo").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Mana: " + player.getMana() + " | Lives: " + player.getLives());
                    if(game.getGameTime() <= 0) {
                        board.getTeam("GameTime").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + "Ending");
                    } else {
                        board.getTeam("GameTime").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Time: " + game.getGameTime());
                    }
                }
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
