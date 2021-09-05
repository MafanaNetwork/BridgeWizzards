package me.TahaCheji.scoreboards;

import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.List;

public class InGameScoreBoard {

    public static void setGameScoreboard(Game game) {
        for(GamePlayer player : game.getPlayers()) {
            player.manaRegen();
            Scoreboard board = Bukkit.getScoreboardManager().getNewScoreboard();
            Objective obj = board.registerNewObjective("BridgeWizzards", "dummy", ChatColor.GRAY + "♧" + ChatColor.GREEN + "BridgeWizzards" + ChatColor.GRAY + "♧");

            Score name = obj.getScore(ChatColor.DARK_PURPLE + "=-=-=-=-BridgeWizzards=-=-=--=");
            name.setScore(16);

            Score emptyText1 = obj.getScore(" ");
            emptyText1.setScore(15);

            Team gameInfo = board.registerNewTeam("GameInfo");
            gameInfo.addEntry(ChatColor.BLACK + "" + ChatColor.BLACK);
            gameInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + " Name: " + game.getName() + " | Mode: " + game.getGameMode().toString() + " | Map: " + game.getMap().getName());
            obj.getScore(ChatColor.BLACK + "" + ChatColor.BLACK).setScore(14);

            Score emptyText2 = obj.getScore("  ");
            emptyText2.setScore(13);

            Team playerInfo = board.registerNewTeam("PlayerInfo");
            playerInfo.addEntry(ChatColor.BLACK + "" + ChatColor.GREEN);
            playerInfo.setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Mana: " + player.getMana() + " | Lives: " + player.getLives());
            obj.getScore(ChatColor.BLACK + "" + ChatColor.GREEN).setScore(12);

            Score emptyText3 = obj.getScore("   ");
            emptyText3.setScore(11);

            Score score7 = obj.getScore(ChatColor.GRAY + "Mafana.us.to");
            score7.setScore(10);

            player.getPlayer().setScoreboard(board);

        }
    }

    public static void updateScoreBoard(Game game) {
        for(GamePlayer player : game.getPlayers()) {
            Scoreboard board = player.getPlayer().getScoreboard();
            board.getTeam("PlayerInfo").setPrefix(ChatColor.GRAY + ">> " + ChatColor.GOLD + "Mana: " + player.getMana() + " | Lives: " + player.getLives());
        }
    }


}
