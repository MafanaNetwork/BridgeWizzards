package me.TahaCheji.gameData;

import me.TahaCheji.Main;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.playerData.PlayerLocation;
import me.TahaCheji.playerData.PlayerStatistics;
import me.TahaCheji.scoreboards.InGameScoreBoard;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import me.TahaCheji.tasks.GameCountdownTask;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
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
    private Location lobbySpawn;


    private GameState gameState = GameState.LOBBY;
    private boolean movementFrozen = false;
    private int gameTime = 300;

    private List<GamePlayer> activePlayers = new ArrayList<>();

    public InGameScoreBoard inGameScoreBoard = new InGameScoreBoard();
    public GameCountdownTask gameCountdownTask = null;


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

    public void joinGame(GamePlayer gamePlayer) {
        if (isState(GameState.LOBBY) || isState(GameState.STARTING) || isState(GameState.ACTIVE)) {
            if (activePlayers.size() == 2) {
                gamePlayer.sendMessage(ChatColor.GOLD + "[Game Manager] " + "Error: This game is active.");
                return;
            }
        }
        if (!map.isLoaded()) {
            map.load();
            World world = map.getWorld();
            p1Location.setWorld(world);
            p2Location.setWorld(world);
            lobbySpawn.setWorld(world);
        }
        if(Main.getInstance().isInGame(gamePlayer.getPlayer())) {
            gamePlayer.sendMessage("You are already in a game");
            return;
        }
        gamePlayer.setMAXMANA(getMana());
        gamePlayer.setMana(getMana());
        gamePlayer.setLives(getLives());
        activePlayers.add(gamePlayer);
        gamePlayer.sendMessage(ChatColor.GOLD + "[Game Manager] " + "[" + activePlayers.size() + "/" + "2" + "]");
        gamePlayer.setPlayerLocation(PlayerLocation.GAMELOBBY);
        if (p1 == null) {
            p1 = gamePlayer;
        } else {
            p2 = gamePlayer;
        }
        gamePlayer.getPlayer().getInventory().clear();
        gamePlayer.getPlayer().getInventory().setArmorContents(null);
        gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.ADVENTURE);
        gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
        gamePlayer.teleport(lobbySpawn);

        Main.getInstance().setGame(gamePlayer.getPlayer(), this);
        inGameScoreBoard.setGameScoreboard(gamePlayer);
        setState(GameState.LOBBY);
        if (activePlayers.size() == 2 && !isState(GameState.STARTING)) {
            setState(GameState.STARTING);
            inGameScoreBoard.updateScoreBoard(this);
            sendMessage(ChatColor.GOLD + "[Game Manager] " + "The game will begin in 20 seconds...");
            Main.getInstance().addActiveGame(this);
            startCountdown();
        }
    }

    public void leaveGame(GamePlayer gamePlayer) throws IOException {
        Player player = gamePlayer.getPlayer();
        if (!Main.getInstance().isInGame(player)) {
            return;
        }
        if (isState(GameState.ACTIVE)) {
            getPlayers().remove(gamePlayer);
            setWinner(getPlayers().get(0), this);
        }
        stopGame();

    }

    public void stopGame() {
        if (inGameScoreBoard != null) {
            inGameScoreBoard.stopUpdating();
        }
        if(gameCountdownTask != null) {
            gameCountdownTask.getGameRunTask().getGameTask().setGameTimer(300);
            gameCountdownTask.getGameRunTask().getGameTask().cancel();
        }
        if (p1 != null && p1.getPlayer() != null) {
            p1.getPlayer().sendMessage(ChatColor.GOLD + "[Game Manager] " + "Game has ended");
            p1.getPlayer().teleport(Main.getInstance().getLobbyPoint());
            p1.getPlayer().getPlayer().getInventory().clear();
            p1.getPlayer().getPlayer().getInventory().setArmorContents(null);
            p1.setPlayerLocation(PlayerLocation.LOBBY);
            Main.getInstance().playerGameMap.remove(p1.getPlayer(), this);
            LobbyScoreBoard lobbyScoreBoard = new LobbyScoreBoard();
            lobbyScoreBoard.setLobbyScoreBoard(p1);
            lobbyScoreBoard.updateScoreBoard(p1);
        }
        if (p2 != null && p2.getPlayer() != null) {
            p2.getPlayer().sendMessage(ChatColor.GOLD + "[Game Manager] " + "Game has ended");
            p2.getPlayer().teleport(Main.getInstance().getLobbyPoint());
            p2.getPlayer().getPlayer().getInventory().clear();
            p2.getPlayer().getPlayer().getInventory().setArmorContents(null);
            p2.setPlayerLocation(PlayerLocation.LOBBY);
            Main.getInstance().playerGameMap.remove(p2.getPlayer(), this);
            LobbyScoreBoard lobbyScoreBoard = new LobbyScoreBoard();
            lobbyScoreBoard.setLobbyScoreBoard(p2);
            lobbyScoreBoard.updateScoreBoard(p2);
        }
        Main.getInstance().getGames().remove(this);
        resetGameInfo();
        Main.getInstance().getGames().add(this);
        Main.getInstance().removeActiveGame(this);
    }

    public void resetGameInfo() {
        p1 = null;
        p2 = null;
        getPlayers().clear();
        gameTime = 300;
        inGameScoreBoard = new InGameScoreBoard();
        map.unload();
    }

    public void setWinner(GamePlayer winner, Game game) throws IOException {
        Bukkit.broadcastMessage(ChatColor.GOLD + "[Game Manager] " + winner.getName() + " won the Game!");
        Economy econ = Main.getEcon();
        econ.depositPlayer(winner.getPlayer(), 100);
        winner.getLevels().addXp(30);
        game.setState(Game.GameState.ENDING);

        PlayerStatistics statistics = winner.getStatistics();
        statistics.setWins(statistics.getWins() + 1);
        winner.setStatistics(statistics);
        statistics.save();

        winner.getPlayer().sendMessage(ChatColor.GOLD + "[Game Manager] " + "+30 xp +100 coins");
        game.stopGame();
    }

    public void assignSpawnPositions() {
        p1.teleport(p1Location);
        p2.teleport(p2Location);
        for (GamePlayer gamePlayer : getPlayers()) {
            gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.SURVIVAL);
            gamePlayer.getPlayer().setHealth(gamePlayer.getPlayer().getMaxHealth());
        }
    }

    public void joinAdmin (GamePlayer gamePlayer) {
        for (Player online : Bukkit.getOnlinePlayers())
            online.hidePlayer(gamePlayer.getPlayer());
        gamePlayer.teleport(lobbySpawn);
        gamePlayer.getPlayer().setGameMode(org.bukkit.GameMode.CREATIVE);
    }

    public void startCountdown() {
       gameCountdownTask = new GameCountdownTask(this);
       gameCountdownTask.runTaskTimer(Main.getInstance(), 0, 20);
    }

    public void setLobbySpawn(Location lobbySpawn) {
        this.lobbySpawn = lobbySpawn;
    }

    public Location getLobbySpawn() {
        return lobbySpawn;
    }

    public void setP1Location(Location p1Location) {
        this.p1Location = p1Location;
    }

    public void setP2Location(Location p2Location) {
        this.p2Location = p2Location;
    }

    public boolean isP1(Player player) {
        return player.getUniqueId().toString().contains(p1.getPlayer().getUniqueId().toString());
    }

    public boolean isP2(Player player) {
        return player.getUniqueId().toString().contains(p2.getPlayer().getUniqueId().toString());
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
        return activePlayers;
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

    public void setGameTime(int gameTime) {
        this.gameTime = gameTime;
    }

    public int getGameTime() {
        return gameTime;
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

    public GamePlayer getGamePlayer(Player player) {
        for (GamePlayer gamePlayer : getPlayers()) {
            if (gamePlayer.getPlayer() == player) {
                return gamePlayer;
            }
        }
        return null;
    }

    public enum GameState {
        LOBBY, STARTING, PREPARATION, ACTIVE, DEATHMATCH, ENDING
    }
}
