package me.TahaCheji.tasks;

import me.TahaCheji.Main;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.data.PlayerLocation;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import org.bukkit.GameMode;
import org.bukkit.scheduler.BukkitRunnable;


public class GameRunTask extends BukkitRunnable {

    private Game game;
    private int startIn = 10;

    public GameRunTask(Game game) {
        this.game = game;
        this.game.setState(Game.GameState.PREPARATION);
        for (GamePlayer gamePlayer : game.getPlayers()) {
            gamePlayer.setPlayerLocation(PlayerLocation.GAMELOBBY);
            gamePlayer.getPlayer().setGameMode(GameMode.ADVENTURE);
        }
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
            for(GamePlayer gamePlayer : game.getPlayers()) {
                //give them kits
                Main.getInstance().setGame(gamePlayer.getPlayer(), game);
                gamePlayer.setPlayerLocation(PlayerLocation.GAME);
                gamePlayer.getPlayer().setGameMode(GameMode.SURVIVAL);
            }
            new ActiveGameTask(game).runTaskTimer(Main.getInstance(), 0, 20);
        } else {
            startIn -= 1;
            this.game.sendMessage("The game will begin in " + startIn + " second" + (startIn == 1 ? "" : "s"));
        }
    }
}