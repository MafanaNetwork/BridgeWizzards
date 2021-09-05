package me.TahaCheji;

import me.TahaCheji.commands.AdminCommand;
import me.TahaCheji.commands.MainCommand;
import me.TahaCheji.data.Game;
import me.TahaCheji.util.Files;
import org.bukkit.ChatColor;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class Main extends JavaPlugin {

    private static Main instance;
    private Set<Game> games = new HashSet<>();
    private Map<Player, Game> playerGameMap = new HashMap<>();

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

        try {
            Files.initFiles();
        } catch (IOException | InvalidConfigurationException e2) {
            e2.printStackTrace();
        }

        getCommand("BridgeAdminWizzards").setExecutor(new AdminCommand());
        getCommand("bz").setExecutor(new MainCommand());
    }

    @Override
    public void onDisable() {
        System.out.println(ChatColor.RED + "Stopping: BridgeWizzards");
    }

    public boolean registerGame(Game game) {

        games.add(game);
        return true;
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

    public static Main getInstance() {
        return instance;
    }
}
