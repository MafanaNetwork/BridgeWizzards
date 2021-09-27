package me.TahaCheji.tasks;

import me.TahaCheji.gameData.Game;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import org.bukkit.scheduler.BukkitRunnable;

public class ActiveGameTask extends BukkitRunnable {

    private Game game;
    private int gameTimer;
    InGameScoreBoard gameScoreBoard = new InGameScoreBoard();

    public ActiveGameTask(Game game, int gameTimer) {
        this.game = game;
        this.gameTimer = gameTimer;
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

    public int getGameTimer() {
        return game.getGameTime();
    }

    public void setGameTimer(int gameTimer) {
        this.gameTimer = gameTimer;
    }
}
