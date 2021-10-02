package me.TahaCheji;

import it.unimi.dsi.fastutil.Hash;
import me.TahaCheji.commands.AdminCommand;
import me.TahaCheji.commands.MainCommand;
import me.TahaCheji.gameData.Game;
import me.TahaCheji.gameData.GameData;
import me.TahaCheji.gameData.GamePlayer;
import me.TahaCheji.mapUtil.GameMap;
import me.TahaCheji.playerData.LevelData;
import me.TahaCheji.playerData.Levels;
import me.TahaCheji.playerData.PlayerLocation;
import me.TahaCheji.itemData.MasterItems;
import me.TahaCheji.scoreboards.LobbyScoreBoard;
import me.TahaCheji.util.Files;
import me.TahaCheji.util.ServerVersion;
import net.kyori.adventure.text.Component;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class Main extends JavaPlugin {

    private static Main instance;
    private ServerVersion version;
    private Set<Game> games = new HashSet<>();
    private Set<Game> activeGames = new HashSet<>();
    public Map<Player, Game> playerGameMap = new HashMap<>();
    public static Set<GamePlayer> players = new HashSet<>();
    private static Economy econ = null;
    public static Map<String, MasterItems> items = new HashMap();
    public static Map<Integer, MasterItems> itemIDs = new HashMap();
    public static List<MasterItems> allItems = new ArrayList<>();
    private static HashMap<Player, GameMap> playerGameMapHashMap = new HashMap<>();
    public static List<GameMap> activeMaps = new ArrayList<>();

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GREEN + "Starting: BridgeWizzards");
        instance = this;
        version = new ServerVersion(Bukkit.getServer().getClass());
        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName, ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        for(Class<?> clazz : new Reflections(packageName).getSubTypesOf(MasterItems.class)) {
            try {
                MasterItems masterItems = (MasterItems) clazz.getDeclaredConstructor().newInstance();
                masterItems.registerItem();
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        try {
            Files.initFiles();
        } catch (IOException | InvalidConfigurationException e2) {
            e2.printStackTrace();
        }

            for (Game game : GameData.getAllSavedGames()) {
                addGame(game);
            }

        if (!setupEconomy()) {
            System.out.print("No econ plugin found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        for(Game game : activeGames) {
            game.stopGame();
        }


        for(Player player : Bukkit.getOnlinePlayers()) {
            player.teleport(getLobbyPoint());
            GamePlayer gamePlayer = new GamePlayer(player, PlayerLocation.LOBBY);
            Levels playerLvl = LevelData.getPlayerLevel(gamePlayer.getPlayer());
            gamePlayer.setLevels(playerLvl);
            playerLvl.setPlayer(gamePlayer);
            addGamePlayer(gamePlayer);
            player.sendMessage(ChatColor.RED + "It is very recommended for you to re join the server this is a reboot");
        }

        for(Game game : activeGames) {
            game.stopGame();
        }

        for(GameMap gameMap : activeMaps) {
            for(Player player : gameMap.getWorld().getPlayers()) {
                player.teleport(getLobbyPoint());
            }
            gameMap.unload();
        }

        getCommand("bzAdmin").setExecutor(new AdminCommand());
        getCommand("bz").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.RED + "Stopping: BridgeWizzards");
    }


    public void addGame(Game game) {
        games.add(game);
    }

    public void addActiveGame(Game game) {
        activeGames.add(game);
    }

    public void removeActiveGame(Game game) {
        activeGames.remove(game);
    }

    public Set<Game> getGames() {
        return games;
    }

    public Set<Game> getActiveGames() {
        return activeGames;
    }

    public Game getActiveGame(String name) {
        Game game = GameData.getGame(name);
        if(activeGames.contains(game)) {
            return game;
        } else {
            return null;
        }
    }

    public Game getGame(String gameName) {
        for (Game game : games) {
            if (game.getName().equalsIgnoreCase(gameName)) {
                return game;
            }
        }

        return null;
    }

    public void addGamePlayer(GamePlayer gamePlayer) {
            players.add(gamePlayer);
    }

    public Game getGame(Player player) {
        return this.playerGameMap.get(player);
    }

    public void setGame(Player player, Game game) {
        if (game == null) {
            this.playerGameMap.remove(player);
        } else {
            this.playerGameMap.put(player, game);
        }
    }

    public boolean isInGame(Player player) {
        if(this.playerGameMap.containsKey(player)) {
            return true;
        } else {
            return false;
        }
    }

    public GamePlayer getPlayer(Player player) {
        GamePlayer gPlayer = null;
        for(GamePlayer gamePlayer : players) {
            if(gamePlayer.getPlayer().getUniqueId().toString().contains(player.getUniqueId().toString())) {
                gPlayer = gamePlayer;
            }
        }
        return gPlayer;
    }


    private Location lobbyPoint = null;
    public Location getLobbyPoint() {
        if (lobbyPoint == null) {
            int x = 0;
            int y = 0;
            int z = 0;
            String world = "world";

            try {
                x = Main.getInstance().getConfig().getInt("lobby-point.x");
                y = Main.getInstance().getConfig().getInt("lobby-point.y");
                z = Main.getInstance().getConfig().getInt("lobby-point.z");
                world = Main.getInstance().getConfig().getString("lobby-point.world");
            } catch (Exception ex) {
                Main.getInstance().getLogger().severe("Lobby point failed with exception: " + ex);
                ex.printStackTrace();
            }

            lobbyPoint = new Location(Bukkit.getWorld(world), x, y, z);
        }

        return lobbyPoint;
    }


    public static MasterItems getItemFromID(int id) {
        MasterItems item = (MasterItems) itemIDs.get(id);

        return item == null ? (MasterItems)items.get("null") : item;
    }

    public static void putItem(String name, MasterItems item) {
            items.put(name, item);
            itemIDs.put(item.getUUID(), item);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static HashMap<Player, GameMap> getPlayerGameMapHashMap() {
        return playerGameMapHashMap;
    }

    public static void addGameMap(Player player, GameMap gameMap) {
        if(playerGameMapHashMap.containsValue(gameMap)) {
            return;
        }
        playerGameMapHashMap.put(player, gameMap);
    }

    public static void removeGameMap (Player player, GameMap gameMap) {
        playerGameMapHashMap.remove(player, gameMap);
    }

    public ServerVersion getVersion() {
        return version;
    }

    public static Economy getEcon() {
        return econ;
    }


    public static Main getInstance() {
        return instance;
    }
}
