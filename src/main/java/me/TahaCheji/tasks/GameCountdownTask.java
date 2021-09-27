package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.gameData.Game;
import org.bukkit.ChatColor;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdownTask extends BukkitRunnable {

    private int time = 20;
    private Game game;
    GameRunTask gameRunTask;

    public GameCountdownTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        time -= 1;
        if (time == 0) {
            // Start
            cancel();

          gameRunTask =  new GameRunTask(game);
          gameRunTask.runTaskTimer(Main.getInstance(), 0, 20);
        } else {
            if (time == 15 || time == 10 || time == 5) {
                game.sendMessage(ChatColor.GOLD + "[Game Manager] " +"You'll be teleported to the game in " + time + " seconds");
            }
        }
    }

    public GameRunTask getGameRunTask() {
        return gameRunTask;
    }
}
