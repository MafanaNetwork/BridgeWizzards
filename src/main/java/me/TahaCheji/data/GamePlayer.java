package me.TahaCheji.data;

import me.TahaCheji.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GamePlayer {

    private Player player = null;
    private Game game;
    private int lives;
    private double mana;
    private final double MAXMANA = getGame().getMana();
    private BukkitTask regen;


    public GamePlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    public void sendMessage(String message) {
            player.sendMessage(message);
    }

    public int getLives() {
        return lives;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public double getMana() {
        return this.mana;
    }

    public void setMana(double mana){
        this.mana = mana;

    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void teleport(Location location) {
        if (location == null) {
            return;
        }
            getPlayer().teleport(location);
        }

    public void manaRegen() {
        this.regen = new BukkitRunnable() {
            @Override
            public void run() {
                if (mana + (0.05 * MAXMANA) > MAXMANA) {
                    // cancel
                } else {
                    // set mana to current mana + (0.02 * maxMana)
                    mana += 0.05 * MAXMANA;
                }
            }
        }.runTaskTimer(Main.getInstance(), 0L, 20L);
    }

    public String getName() {
            return player.getDisplayName();
    }
}
