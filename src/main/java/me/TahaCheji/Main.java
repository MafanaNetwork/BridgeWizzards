package me.TahaCheji;

import me.TahaCheji.commands.AdminCommand;
import me.TahaCheji.commands.MainCommand;
import me.TahaCheji.data.Game;
import me.TahaCheji.data.GameData;
import me.TahaCheji.data.GamePlayer;
import me.TahaCheji.util.Files;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Set<Game> games = new HashSet<>();
    private Set<Game> activeGames = new HashSet<>();
    private Map<Player, Game> playerGameMap = new HashMap<>();
    public static Set<GamePlayer> players = new HashSet<>();
    private static Economy econ = null;

    @Override
    public void onEnable() {
        System.out.println(ChatColor.GREEN + "Starting: BridgeWizzards");
        instance = this;

        String packageName = getClass().getPackage().getName();
        for (Class<?> clazz : new Reflections(packageName, ".listeners").getSubTypesOf(Listener.class)) {
            try {
                Listener listener = (Listener) clazz.getDeclaredConstructor().newInstance();
                getServer().getPluginManager().registerEvents(listener, this);
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }

        for(Game game : GameData.getAllSavedGames()) {
            addGame(game);
        }

        try {
            Files.initFiles();
        } catch (IOException | InvalidConfigurationException e2) {
            e2.printStackTrace();
        }

        if (!setupEconomy()) {
            System.out.print("No econ plugin found.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("bzAdmin").setExecutor(new AdminCommand());
        getCommand("bz").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        for(Game game : activeGames) {
            game.stopGame();
        }
        System.out.println(ChatColor.RED + "Stopping: BridgeWizzards");
    }

    public void addGame(Game game) {
        games.add(game);
    }

    public void addActiveGame(Game game) {
        activeGames.add(game);
    }

    public void removeActiveGame(Game game) {
        if(activeGames.contains(game)) {
            activeGames.remove(game);
        } else {
            System.out.println("Could not remove active game");
        }
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

    public static Economy getEcon() {
        return econ;
    }


    public static Main getInstance() {
        return instance;
    }
}
