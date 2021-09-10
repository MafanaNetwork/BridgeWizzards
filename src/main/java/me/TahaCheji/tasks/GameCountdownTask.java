package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import org.bukkit.scheduler.BukkitRunnable;

public class GameCountdownTask extends BukkitRunnable {

    private int time = 20;
    private Game game;

    public GameCountdownTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        time -= 1;
        if (time == 0) {
            // Start
            cancel();

            new GameRunTask(game).runTaskTimer(Main.getInstance(), 0, 20);
//            Skywars.getInstance().getServer().getScheduler().runTask(Skywars.getInstance(), new GameRunTask(game)); // Deprecated
        } else {
            if (time == 15 || time == 10 || time == 5) {
                game.sendMessage("You'll be teleported to the game in " + time + " seconds");
            }
        }
    }
}
