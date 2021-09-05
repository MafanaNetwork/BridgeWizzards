package me.TahaCheji.data;

import me.TahaCheji.Main;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.tasks.GameCountdownTask;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Game {

    private GamePlayer p1;
    private GamePlayer p2;

    private final String name;
    private final ItemStack gameIcon;
    private final GameMode gameMode;
    private final int mana;
    private final int lives;
    private final GameMap map;
    private Location p1Location;
    private Location p2Location;

    private GameState gameState = GameState.LOBBY;
    private boolean movementFrozen = false;


    public Game(String name, ItemStack gameIcon, GameMode gameMode, int mana, int lives, GameMap map) {
        this.name = name;
        this.gameIcon = gameIcon;
        this.gameMode = gameMode;
        this.mana = mana;
        this.lives = lives;
        this.map = map;
    }

    public void saveGame() throws IOException {
        GameData.saveGame(this);
    }

    public boolean joinGame(GamePlayer gamePlayer) {
        if (isState(GameState.LOBBY) || isState(GameState.STARTING)) {
            if (getPlayers().size() == 2) {
                gamePlayer.sendMessage("Error: This game is full.");
                return false;
            }
        }
        getPlayers().add(gamePlayer);
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().getInventory().setArmorContents(null);
        gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.ADVENTURE);
        gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
        if (getPlayers().size() == 2 && !isState(GameState.STARTING)) {
            setState(GameState.STARTING);
            sendMessage("The game will begin in 20 seconds...");
            startCountdown();
        }
        Main.getInstance().setGame(gamePlayer.getPlayer(), this);
        return true;
    }


    public void startGame() {
        p1.getPlayer().getInventory().clear();
        p1.getPlayer().getInventory().setArmorContents(null);
        p2.getPlayer().getInventory().clear();
        p2.getPlayer().getInventory().setArmorContents(null);

        map.load();
        World world = map.getWorld();
        Location newP1Location = new Location(world, p1Location.getX(), p1Location.getY(), p1Location.getZ());
        p1.teleport(newP1Location);
        Location newP2Location = new Location(world, p2Location.getX(), p2Location.getY(), p2Location.getZ());
        p2.teleport(newP2Location);
        //stop movement
        //start game count down

    }

    public void stopGame() {
        map.unload();
        for(GamePlayer player : getPlayers()) {
            player.sendMessage("Game has ended");
            player.teleport(new Location(Bukkit.getWorld("world") ,0, 0, 0));
            player.getPlayer().getInventory().clear();
            player.getPlayer().getInventory().setArmorContents(null);
        }

    }

    public void startCountdown() {
        new GameCountdownTask(this).runTaskTimer(Main.getInstance(), 0, 20);
    }

    public Location assignSpawnPositions() {
        p1.teleport(p1Location);
        p2.teleport(p2Location);
        for(GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
            gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
        }
        return null;
    }

    public void setP1(GamePlayer p1) {
        this.p1 = p1;
    }

    public void setP2(GamePlayer p2) {
        this.p2 = p2;
    }

    public void setP1Location(Location p1Location) {
        this.p1Location = p1Location;
    }

    public void setP2Location(Location p2Location) {
        this.p2Location = p2Location;
    }

    public String getName() {
        return name;
    }

    public ItemStack getGameIcon() {
        return gameIcon;
    }

    public int getMana() {
        return mana;
    }

    public int getLives() {
        return lives;
    }

    public GameMap getMap() {
        return map;
    }

    public GamePlayer getP1() {
        return p1;
    }

    public GamePlayer getP2() {
        return p2;
    }

    public List<GamePlayer> getPlayers() {
        List<GamePlayer> getPlayers = new ArrayList<>();
        getPlayers.add(p1);
        getPlayers.add(p2);
        return getPlayers;
    }

    public boolean isState(GameState state) {
        return getGameState() == state;
    }

    public void setState(GameState gameState) {
        this.gameState = gameState;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void sendMessage(String message) {
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.sendMessage(message);
        }
    }

    public void setMovementFrozen(boolean movementFrozen) {
        this.movementFrozen = movementFrozen;
    }

    public boolean isMovementFrozen() {
        return movementFrozen;
    }

    public Location getP1Location() {
        return p1Location;
    }

    public Location getP2Location() {
        return p2Location;
    }

    public GameMode getGameMode() {
        return gameMode;
    }

    public enum GameState {
        LOBBY, STARTING, PREPARATION, ACTIVE, DEATHMATCH, ENDING
    }
}
