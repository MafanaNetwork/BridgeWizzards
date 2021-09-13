package me.TahaCheji.tasks;

import me.TahaCheji.data.Game;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveGameTask extends BukkitRunnable {

    private Game game;
    private int gameTimer = 300;
    InGameScoreBoard gameScoreBoard = new InGameScoreBoard();

    public ActiveGameTask(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        if(this.gameTimer <= 0) {
            this.cancel();
            game.stopGame();
            gameScoreBoard.stopUpdating();
        } else {
            gameTimer --;
            game.setGameTime(gameTimer);
        }
    }
}
