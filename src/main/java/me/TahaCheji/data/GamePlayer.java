package me.TahaCheji.data;

import me.TahaCheji.Main;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class GamePlayer {

    private final Player player;
    private Game game;
    private int lives;
    private double mana = 0;
    private double MAXMANA;
    private BukkitTask regen;
    private PlayerLocation playerLocation;


    public GamePlayer(Player player, Game game) {
        this.player = player;
        this.game = game;
    }

    public GamePlayer(Player player, PlayerLocation location) {
        this.player = player;
        this.playerLocation = location;
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

    public Game getGame() {
        return game;
    }

    public double getMana() {
        return this.mana;
    }

    public void setMana(double mana){
        this.mana = mana;
    }

    public PlayerLocation getPlayerLocation() {
        return playerLocation;
    }

    public void setMAXMANA(double MAXMANA) {
        this.MAXMANA = MAXMANA;
    }

    public void setPlayerLocation(PlayerLocation playerLocation) {
        this.playerLocation = playerLocation;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setGame(Game game) {
        this.game = game;
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
