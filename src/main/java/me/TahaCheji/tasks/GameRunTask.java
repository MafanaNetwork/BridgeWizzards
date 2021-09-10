package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import org.bukkit.scheduler.BukkitRunnable;


public class GameRunTask extends BukkitRunnable {

    private Game game;
    private int startIn = 10;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        this.game.assignSpawnPositions();
        this.game.sendMessage("You've been teleported.");
        this.game.sendMessage("The game will begin in " + this.startIn + " seconds...");
        this.game.setMovementFrozen(true);
    }

    @Override
    public void run() {
        if (startIn <= 1) {
            this.cancel();
            this.game.setState(Game.GameState.ACTIVE);
            this.game.sendMessage("The game has started.");
            this.game.setMovementFrozen(false);
            new ActiveGameTask(game).runTaskTimer(Main.getInstance(), 0, 20);
        } else {
            startIn -= 1;
            this.game.sendMessage("The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s"));
        }
    }
}